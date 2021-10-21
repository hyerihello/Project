package Entity;
// 데이터 베이스 테이블의 속성을 가진 클래스
public class EmpEntity {

	
private String id;
private String pw;
private String eml;
private String tel;




public EmpEntity() {
	super();
	
}

public EmpEntity(String id, String pw, String eml, String tel) {
	super();
	this.id = id;
	this.pw = pw;
	this.eml = eml;
	this.tel = tel;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getPw() {
	return pw;
}

public void setPw(String pw) {
	this.pw = pw;
}

public String getEml() {
	return eml;
}

public void setEml(String eml) {
	this.eml = eml;
}

public String getTel() {
	return tel;
}

public void setTel(String tel) {
	this.tel = tel;
}
}
