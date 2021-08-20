# 라이브러리 등록

# BeautifulSoup :: 읽어 온 웹페이지를 파싱한다.
# requests :: 작은 웹브라우저로 웹사이트 내용을 가져온다.
# urllib :: 웹과 관련된 데이터를 쉽게 이용하도록 한다.
# urllib.parse :: URL 파싱과 관련된 것.
# cx_Oracle :: python-oracle db연동 시킨다.
# tkinter :: 파이썬에서 제공하는 GUI 툴 제공한다. (기본내장모듈)
# PIL (pillow) :: 사진과 관련된 여러 함수를 가지고 있다.

import cx_Oracle
from bs4 import BeautifulSoup
import urllib.parse as par
import requests
from tkinter import *
from PIL import Image, ImageTk
import tkinter


class DbClass:
    # 생성자호출
    def __init__(self): 
        self.dsn = cx_Oracle.makedsn('localhost', 1521, 'xe') # 오라클 주소기입
        self.db = cx_Oracle.connect('SCOTT', 'TIGER', self.dsn) # 오라클 접속 유저정보
        self.cur = self.db.cursor() # 커서객체 가져오기
        
    # Table 생성    
    def Create_Table(self):
        sql_cmd = "CREATE TABLE GUI_DB(PNAME VARCHAR2(1000), IMG_LINK VARCHAR2(1000), RATING NUMBER, REVIEW_NUM NUMBER, PRO_LINK VARCHAR2(1000))"
        self.cur.execute(sql_cmd) # SQL문장실행
        self.db.close()
    
    # 지정한 키워드 상품 크롤링 하기
    def Coupang(self):
        search = '빔프로젝터'
        rating_num = 4.5
        review_num = 100
        encoded = par.quote(search)
        pg_num = 1
        row_num = 1
        while True:
            if pg_num == 5 # 5페이지 
                break
            url = "https://www.coupang.com/np/search?q={}&channel=user&component=&eventCategory=SRP&trcid=&traid=&sorter=scoreDesc&minPrice=&maxPrice=&priceRange=&filterType=&listSize=36&filter=&isPriceRange=false&brand=&offerCondition=&rating=0&page={}&rocketAll=false&searchIndexingToken=1=4&backgroundColor=".format(
                encoded, pg_num)
            code = requests.get(url, headers={"User-Agent": "Mozilla/5.0"})
            soup = BeautifulSoup(code.text, "html.parser")
            
            # html에 있는 "search-product" 의 "li" 가져온다 
            all = soup.find_all("li", class_="search-product")
            name_list = []
            imlink_list = []
            rating_list = []
            review_list = []
            prlink_list = []
            
            # 가져온 all 내에 있는 상품명, 별점, 리뷰, 링크, 이미지를 검색한다.
            for i in all:
                name = i.find("div", class_="name")
                rating = i.find("em", class_="rating")
                review = i.find("span", class_="rating-total-count")
                link = i.find("a")
                img = i.find("img", class_="search-product-wrap-img")
                if rating:
                    rating_f = rating.get_text(" ", strip=True)
                else:
                    continue
                if review:
                    review_f = review.get_text(" ", strip=True).replace("(", "").replace(")", "")
                else:
                    continue
                    
                # 입력한 별점과 리뷰수와 웹에서 가져온 별점과 리뷰수가 같거나 큰 제품을 찾아 상품명을 가져온다.     
                if float(rating_f) >= float(rating_num) and float(review_f) >= float(review_num):
                    name_f = name.get_text(" ",strip = True)
                    name_list.append(name_f) # 결과를 list 객체에 담는다
                    print("[", pg_num, "페이지", "]")
                    print("상품명 :", row_num, name_f)                    
                    rating_list.append(rating_f)                    
                    print("평점 :", rating_f)                    
                    review_list.append(review_f)
                    print("리뷰수 :", review_f)                    
                    link_f = "https://www.coupang.com" + link["href"]
                    prlink_list.append(link_f)
                    print("링크 :", link_f)
                    print("===============================================================")
                    img_f = "https:" + img["src"]
                    if ".gif" in img_f:
                        img_f = "https:" + img["data-img-src"]
                    imlink_list.append(img_f)
                    row_num += 1
            pg_num += 1
            return [name_list, imlink_list, rating_list, review_list, prlink_list]
          
          
    # 오라클 DB 연동 후 GUI_DB 테이블에 크롤링 DB 적재시킨다
    def Insert(self): 
        for i, j, k, l, m in zip(self.Coupang()[0], self.Coupang()[1], self.Coupang()[2], self.Coupang()[3], self.Coupang()[4]):
            self.dsn = cx_Oracle.makedsn('localhost', 1521, 'xe')
            self.db = cx_Oracle.connect('SCOTT', 'TIGER', self.dsn)
            self.cur = self.db.cursor()
            my_dict = [(i, j, k, l, m)]
            query = "INSERT INTO GUI_DB(PNAME, IMG_LINK, RATING, REVIEW_NUM, PRO_LINK) VALUES(:1, :2, :3, :4, :5)"
            self.cur.executemany(query, my_dict)
            self.db.commit()
            self.db.close()
    
    
    # 저장된 DB 조회        
    def Select_all(self):
        self.cur.execute("SELECT * FROM GUI_DB")
        result = self.cur.fetchall()
        for row in result:
            print(str(row[0]) + ", " + row[1] + ", " + str(row[2]) + ", " + str(row[3]) + ", " + row[4])
            print()
        self.cur.close()
        self.db.close()
        
    # 프로시저 호출     
    def Select_pro(self):
        res = self.cur.var(cx_Oracle.CURSOR)
        self.cur.callproc("GUI_PRO01", [res])
        res = res.getvalue()
        ima = []
        for i in res:
            ima.append(i[1])
        print(ima)
    
    # Tkinter 로 gui화면 구현 
    def make_gui(self):
        self.screen = Tk()
        self.screen.title("영며들다! 5_team")
        self.screen.geometry("600x300+200+200")
        self.screen.resizable(True, True)
        
        lab = Label(self.screen, text="Top3 제품확인", anchor="nw", font="맑은고딕 18 bold").grid(row=0, padx=5, pady=8)
        self.na = Label(self.screen, text="제품명:").grid(row=1, padx=5, pady=8)
        self.re = Label(self.screen, text="리뷰수:").grid(row=2, padx=5, pady=8)
        self.st = Label(self.screen, text="별점:").grid(row=3, padx=5, pady=8)
        
        
        # 엔트리 위젯을 이용해 텍스트를 입력받을때 사용
        # 변수명 = Entry(부모, 옵션).grid(몇번째 줄, 좌우 어디에 위치?)
        
        self.en_na = Entry(self.screen)
        self.en_na.grid(row=1, column=1)
        
        self.en_re = Entry(self.screen)
        self.en_re.grid(row=2, column=1)
        
        self.en_st = Entry(self.screen)
        self.en_st.grid(row=3, column=1)

        Button(self.screen, text='확인', cursor="hand2", command = new_1).grid(row=4, column=2, sticky=W, padx=5, pady=5)
        self.screen.mainloop()

# 새로운창 띄우기
def new_1():
    dsn = cx_Oracle.makedsn('localhost', 1521, 'xe')
    db = cx_Oracle.connect('SCOTT', 'TIGER', dsn)
    cur = db.cursor()
    test_1 = tkinter.Toplevel()
    test_1.title("영며들다! 5_team")
    test_1.geometry('1000x350')
    ima = []
    res = cur.var(cx_Oracle.CURSOR)
    cur.callproc("GUI_PRO01", [res])
    res = res.getvalue()
    for i in res:
        ima.append(i)
            # print(type(ima[0][0]))
    load = Image.open("1.jpg")
    render = ImageTk.PhotoImage(load)
    img = Label(test_1, image=render)
    img.image = render
    img.place(x=100, y=10)
    img01 = Label(test_1, text=ima[0][0])
    img01.place(x=110, y=280)
    load1 = Image.open("2.jpg")
    render1 = ImageTk.PhotoImage(load1)
    img = Label(test_1, image=render1)
    img.image = render1
    img.place(x=400, y=10)
    img02 = Label(test_1, text=ima[1][0])
    img02.place(x=400, y=280)
    load2 = Image.open("3.jpg")
    render2 = ImageTk.PhotoImage(load2)
    img = Label(test_1, image=render2)
    img.image = render2
    img.place(x=700, y=10)
    img02 = Label(test_1, text=ima[2][0])
    img02.place(x=650, y=280)
if __name__ == '__main__':
    # 차례로 주석 풀고 실행해야 함
    test = DbClass()
    # test.Create_Table()
    # test.Coupang()
    # test.Insert()
    # test.Select_all()
    # test.Select_pro()
    test.make_gui()
