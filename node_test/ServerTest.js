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
console.log(connection);

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
        else testQuery = "SELECT * FROM test WHERE testaddr like '%" + parsedQuery["str"] + "%'";

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
});

server.listen(8080, function(){
    console.log('Server is running...');
});