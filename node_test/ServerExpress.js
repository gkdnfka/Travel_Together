// @솔빈 2022-03-09 (수)
// url 파싱, 쿼리 파싱, mysql 통신을 위한 모듈
// 집합

var url = require('url');
var querystring = require('querystring'); 
const mysql = require('mysql');  // mysql 모듈 로드
const conn = {  // mysql 접속 설정
    host: '13.124.93.31',
    port: '3306',
    user: 'ID',
    password: '1234',
    database: 'CapDB'
};


// @솔빈 2022-03-09 (수)
// express 기반 서버 구축 기초 코드

var connection = mysql.createConnection(conn); // DB 커넥션 생성
connection.connect();

const express = require('express');
const app = express();
const port = 8080

app.get('*', (request, response) => {
    console.log(request.url)
    var tmp_parsed = decodeURI(request.url);
    var parsedUrl = url.parse(tmp_parsed);
    console.log(request.method)
    var parsedQuery = querystring.parse(parsedUrl.path,'/', '=');
    
    console.log(parsedQuery)
    console.log('첫번째 문자 : ' + parsedQuery["func"])
    response.writeHead(200, {'Content-Type':'html/text'});


    if(parsedQuery["func"] == "SearchPlace"){ // 장소를 검색하기 위한 쿼리 파싱
        var testQuery = ""
        if(parsedQuery["type"] == "ByName") testQuery = "SELECT * FROM test WHERE testname like '%" + parsedQuery["str"] + "%'";
        else if(parsedQuery["type"] == "ByAddr") testQuery = "SELECT * FROM test WHERE testaddr like '%" + parsedQuery["str"] + "%'";
        else if(parsedQuery["type"] == "ByIds") testQuery = "SELECT * FROM test WHERE " + parsedQuery["str"];

        var result;
        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }
            console.log(results)
            result = JSON.stringify(results)
            response.end(result)
            console.log("쿼리문 결과1 : " + result)
        });
    }

    if(parsedQuery["func"] == "SearchPost"){ // 게시글을 불러오기 위한 쿼리 파싱
        var testQuery = ""
        if(parsedQuery["type"] == "default") testQuery = "SELECT * FROM NOTICEBOARD_DB";
        else if (parsedQuery["type"] == "ByPostName") testQuery = testQuery = "SELECT * FROM NOTICEBOARD_DB WHERE NOTICEBOARD_TITLE like '%" + parsedQuery["content"] + "%'"; 
        else if (parsedQuery["type"] == "ByUserName") testQuery = testQuery = "SELECT * FROM NOTICEBOARD_DB WHERE USER_NAME like '%" + parsedQuery["content"] + "%'"; 
        var result;
        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }
            console.log(results)
            result = JSON.stringify(results)
            response.end(result)
            console.log("쿼리문 결과 : " + result)
        }); 
    }

    // @정지원 2022-02-22 화
    // 게시글 작성을 위한 로직
    if(parsedQuery["func"] == "PostWrite"){
        var testQuery = "";
        var userdata = parsedQuery["userdata"].split(']');
        var postmain = parsedQuery["postmain"].split(']');
        var user_code = userdata[0];
        var user_name = userdata[1];
        var post_title = postmain[0];
        var post_content = postmain[1];
        var post_date = new Date().toISOString().split('T');
        var course = parsedQuery["coursedata"];
        console.log(post_date[1])
        if(parsedQuery["type"] == "default") testQuery = "INSERT INTO NOTICEBOARD_DB (USER_CODE,USER_NAME,RATE,DATES,COURSE,CONTENT,NOTICEBOARD_TITLE) VALUES (?,?,?,?,?,?,?)";
        var params = [user_code,user_name,'0.0',post_date[0],course,post_content,post_title];
        connection.query(testQuery,params,function (err, results, fields) { // testQuery 실행
            console.log(err);
            if (err) {
                console.log(err);
            }
            console.log(results);
            result = JSON.stringify(results);
            console.log("쿼리문 결과 : " + result);
        });
    }
     // @정지원 2022-03-26 토 
    // 게시글 가져오기(저장)을 위한 로직
    if(parsedQuery["func"] == "BringPostPut"){
        var testQuery = "";
        var postname = parsedQuery["postname"]
        var usercode = parsedQuery["usercode"]
        var course = parsedQuery["coursedata"];
        if(parsedQuery["type"] == "default") testQuery = "INSERT INTO UserTravel_DB (POST_NAME,USER_CODE,COURSE,DAY_INDEX,COURSE_INDEX) VALUES (?,?,?,?,?)";
        var params = [postname,usercode,course,0,0];
        connection.query(testQuery,params,function (err, results, fields) { // testQuery 실행
            console.log(err);
            if (err) {
                console.log(err);
            }
            console.log(results);
            result = JSON.stringify(results);
            console.log("쿼리문 결과 : " + result);
        });
    }
        // @정지원 2022-03-26 토 
    // 게시글 가져오기(불러오기)을 위한 로직
    if(parsedQuery["func"] == "BringPostGet"){
        var testQuery = "";
        if(parsedQuery["type"] == "default") testQuery = "SELECT * FROM  UserTravel_DB Where USER_CODE like'%" + parsedQuery["usercode"] + "%'";
        var result;
        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }
            console.log(results)
            result = JSON.stringify(results)
            response.end(result)
            console.log("쿼리문 결과 : " + result)
        }); 
    }
    // @솔빈 2022-03-03 목 
    // 로그인을 위한 로직
    if(parsedQuery["func"] == "Login"){ 
        var testQuery = "SELECT * FROM USER_DB WHERE USER_NICKNAME = '" + parsedQuery['id'] + "' and USER_PASS = '" + parsedQuery['password'] + "'"
        var result;
        var ret = {
            "number" : "-1",
            "name" : "",
            "code" : ""
        };

        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }

            if(results.length == 1) {
                ret.number = "1";
                ret.code = results[0]["USER_CODE"];
                ret.name = results[0]["USER_NAME"];
            }

            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        }); 
    }

    // @솔빈 2022-03-03 목 
    // 회원가입을 위한 로직
    if(parsedQuery["func"] == "Join"){ 
        var testQuery = "SELECT * FROM USER_DB WHERE USER_NICKNAME = '" + parsedQuery['id'] + "'"
        var result;
        var flag = 1;
        var ret = {
            "number" : "1"
        };

        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
                ret.number = "-2"
            }
            if(results.length >= 1) ret.number = "-1", flag = 0;
            
            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("아이디 조회 결과 : " + ret);
        }); 

        if(flag){
            var testQuery = "INSERT INTO USER_DB VALUES (0, '" + parsedQuery['name'] + "' , '" + parsedQuery['id'] + "' , '" 
            + parsedQuery['password'] + "' , '" + parsedQuery['address'] + "' , '" + parsedQuery['gender'] + "' , '" + parsedQuery['taste'] +"')"

            connection.query(testQuery, function (err, results, fields) { // testQuery 실행
                if (err) {
                    console.log(err);
                    ret.number = "-2"
                }
   
                ret = JSON.stringify(ret);
                response.end(ret);
                console.log("회원가입 결과 : " + ret);
            }); 
        }
    }

    // @솔빈 2022-03-03 목 
    // 댓글을 불러오기 위한 로직
    if(parsedQuery["func"] == "SearchComment"){ 
        var testQuery = "SELECT * FROM COMMENT_DB WHERE UNIQUE_NUM = '" + parsedQuery['number'] + "' and COMMENT_TYPE = '" + parsedQuery['CommentType'] + "'"
        var result;
        console.log(testQuery)

        connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }

            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        }); 
    }

    // @솔빈 2022-03-03 목 
    // 댓글 수정을 위한 로직
    if(parsedQuery["func"] == "EditComment"){ 
        var testQuery = "UPDATE COMMENT_DB SET CONTENT = '" + parsedQuery['Content'] + "' , DATES = '" + parsedQuery['Dates'] + "', COMMENT_RATING = '" +  parsedQuery['Rating'] + "' WHERE COMMENT_NUM = " + parsedQuery['CommentNum']
        var ret = {
            "number" : "1"
        };

        var result;
        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
                ret.number = "-1"
            }

            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        }); 
    }

    

    // @솔빈 2022-03-03 목 
    // 댓글 작성을 위한 로직
    if(parsedQuery["func"] == "WriteComment"){ 
        var testQuery = "INSERT INTO COMMENT_DB VALUES (0, " + parsedQuery['UniqueNum'] + " , " + parsedQuery['UserCode'] + " , '" 
                                                             + parsedQuery['UserName'] + "' , '" + parsedQuery['Dates'] + "' , '" + parsedQuery['Content'] + "' , '" + parsedQuery['CommentType'] + "' , '" + parsedQuery['Rating'] +"')"
        var ret = {
            "number" : "1"
        };

        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
                ret.number = "-1"
            }
            
            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        }); 
    }

    // @솔빈 2022-03-03 목 
    // 댓글 삭제를 위한 로직
    if(parsedQuery["func"] == "DeleteComment"){ 
        var testQuery = "DELETE FROM COMMENT_DB WHERE COMMENT_NUM = " + parsedQuery['CommentNum']
        var ret = {
            "number" : "1"
        };

        connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
            if (err) {
                console.log(err);
                ret.number = "-1"
            }

            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        }); 
    }

     // @솔빈 2022-03-13 일 
     // 취향 불러오기
    if(parsedQuery["func"] == "LoadTaste"){ 
        var testQuery = "SELECT USER_TASTE, USER_GENDER FROM USER_DB WHERE USER_CODE = '" + parsedQuery['code'] + "'"
        var result;
        connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
            if (err) { console.log(err);}
            ret = JSON.stringify(ret[0]);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        }); 
    }
});


// @솔빈 2022-03-09 (수)
// POST 명령 모음
const multer = require('multer')
const path = require('path');
var bodyParser = require('body-parser')
var fs = require('fs');

const storage = multer.diskStorage({
	destination: (req, file, cb) => {  // 파일이 업로드될 경로 설정
		cb(null, 'uploads/')
	},
	filename: (req, file, cb) => {	// timestamp를 이용해 새로운 파일명 설정
		let newFileName = file.originalname
		cb(null, newFileName)
	},
})
const upload = multer({ storage: storage })

app.use(bodyParser.json())

app.post('/upload', upload.single('upload'), (req, res) => {
     console.log(req);
});

app.post('/load*', upload.single('load'), (req, res) => {
    var type = req.body.type
    var number = req.body.number

    
    console.log(req.method)
    var filepath = type + "/" + number
    if(type == "ProfileImages") filepath += '_Profile.jpeg'
    else if(type == "PlaceImages") filepath += '.jpeg'

    fs.readFile(filepath, function(err, data) {
        console.log(filepath);
        console.log(data);
        res.end(JSON.stringify(data));
    });
});


app.listen(port, () => {
    console.log(`server is listening at localhost:${port}`);
});