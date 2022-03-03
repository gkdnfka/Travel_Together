var http = require('http');
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

var connection = mysql.createConnection(conn); // DB 커넥션 생성
connection.connect();

var server = http.createServer(function(request,response){
    var tmp_parsed = decodeURI(request.url);
    var parsedUrl = url.parse(tmp_parsed);
    var parsedQuery = querystring.parse(parsedUrl.path,'/', '=');
    
    console.log(parsedQuery)
    console.log('첫번째 문자 : ' + parsedQuery["func"])
    response.writeHead(200, {'Content-Type':'html/text'});


    if(parsedQuery["func"] == "SearchPlace"){ // 장소를 검색하기 위한 쿼리 파싱
        var testQuery = ""
        if(parsedQuery["type"] == "ByName") testQuery = "SELECT * FROM test WHERE testname = '%" + parsedQuery["str"] + "%'";
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
        else testQuery = "SELECT * FROM test WHERE testaddr like '%" + parsedQuery["str"] + "%'";

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
            if(results.length >= 1) ret.number = "-1", flag = 0
            
            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("아이디 조회 결과 : " + ret);
        }); 

        if(flag){
            var testQuery = "INSERT INTO USER_DB VALUES (0, '" + parsedQuery['name'] + "' , '" + parsedQuery['id'] + "' , '" 
            + parsedQuery['password'] + "' , '" + parsedQuery['address'] + "' , '" + parsedQuery['gender'] + "')"

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
        var testQuery = "SELECT * FROM COMMENT_DB WHERE NOTICEBOARD_NUM = '" + parsedQuery['number'] + "'"
        var result;

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
        var testQuery = "UPDATE COMMENT_DB SET CONTENT = '" + parsedQuery['Content'] + "' , DATES = '" + parsedQuery['Dates'] + "' WHERE COMMENT_NUM = " + parsedQuery['CommentNum']
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
        var testQuery = "INSERT INTO COMMENT_DB VALUES (0, " + parsedQuery['NoticeNum'] + " , " + parsedQuery['UserCode'] + " , '" 
                                                             + parsedQuery['UserName'] + "' , '" + parsedQuery['Dates'] + "' , '" + parsedQuery['Content'] + "')"
        var result;
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
    // 댓글 삭제를 위한 로직
    if(parsedQuery["func"] == "DeleteComment"){ 
        var testQuery = "DELETE FROM COMMENT_DB WHERE COMMENT_NUM = " + parsedQuery['CommentNum']
        console.log("여기까지")
        var result;
        connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }

            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        }); 
    }

});

server.listen(8080, function(){
    console.log('Server is running...');
});