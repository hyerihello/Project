# 라이브러리 등록

# BeautifulSoup :: 읽어 온 웹페이지를 파싱한다.
# requests :: 작은 웹브라우저로 웹사이트 내용을 가져온다.
# urllib :: 웹과 관련된 데이터를 쉽게 이용하도록 한다.
# urllib.request :: 데이터를 읽어오는 역할을 한다.
# urllib.parse :: URL 파싱과 관련된 것.

from bs4 import BeautifulSoup
import urllib.request as req
import urllib.parse as par
import requests
import os

def Coupang(x, y, z):
    search = x
    rating_num = y
    review_num = z
    
    # urllib.parse.quote() :: 아스키코드 형식이 아닌 글자를 URL 인코딩 시켜준다.
    encoded = par.quote(search) 
    if not os.path.exists("./coupang_img"): # os.path.exists :: 폴더가 실제 존재하는지 확인
        os.mkdir("./coupang_img") # os.mkdir :: 폴더 생성
    pg_num = 1 # 페이지 수를 1로 초기화
    row_num = 1 # 상품이 출력되는 번호를 1로 초기화
    
    # 크롤링 할 사이트
    
    while True:
        if pg_num == 11: # 11페이지동안 크롤링하고 
            break # 끝나면 멈춘다
        url = "https://www.coupang.com/np/search?q={}&channel=user&component=&eventCategory=SRP&trcid=&traid=&sorter=scoreDesc&minPrice=&maxPrice=&priceRange=&filterType=&listSize=36&filter=&isPriceRange=false&brand=&offerCondition=&rating=0&page={}&rocketAll=false&searchIndexingToken=1=4&backgroundColor=".format(
            encoded, pg_num)
        code = requests.get(url, headers={"User-Agent": "Mozilla/5.0"})
        soup = BeautifulSoup(code.text, "html.parser")
        
        # html에 있는 "search-product" 의 "li" 가져온다 
        all = soup.find_all("li", class_="search-product")
        
        # 가져온 all 내에 있는 상품명, 별점, 리뷰, 링크, 이미지를 검색한다.
        for i in all:
            name = i.find("div", class_="name")
            rating = i.find("em", class_="rating")
            review = i.find("span", class_="rating-total-count")
            link = i.find("a")
            img = i.find("img", class_="search-product-wrap-img")
            if rating:
                rating_f = rating.get_text(" ", strip=True) # 텍스트만 가져오기
            else:
                continue
            if review:
                review_f = review.get_text(" ", strip=True).replace("(", "").replace(")", "") # 텍스트만 가져오기, ()는 공백으로 변경
            else:
                continue
            # 입력한 별점과 리뷰수와 웹에서 가져온 별점과 리뷰수가 같거나 큰 제품을 찾아 상품명을 가져온다.               
            if float(rating_f) >= float(rating_num) and float(review_f) >= float(review_num):
                name_f = name.get_text(" ",strip = True) # 텍스트만 가져온 후
                
                print("[", pg_num, "페이지", "]") # 검색한 페이지 번호
                print("상품명 :", row_num, name_f) # 상품출력번호, 상품명
                print("평점 :", rating_f) # 별점
                print("리뷰수 :", review_f) # 리뷰수
                link_f = "https://www.coupang.com" + link["href"]
                print("링크 :", link_f)
                print("===============================================================")
                img_f = "https:" + img["src"]
                if ".gif" in img_f:
                    img_f = "https:" + img["data-img-src"]
                # urllib.request.urlretrieve :: 파일에 자료를 입력할 수 있다.    
                req.urlretrieve(img_f, "./coupang_img/{}.jpg".format(row_num))
                row_num += 1
        pg_num += 1
if __name__ == '__main__':
    Coupang(input("어떤 상품을 원하니>>"), input("평점 몇 점 이상을 원하니>>"), input("리뷰수 몇 개 이상을 원하니>>"))
