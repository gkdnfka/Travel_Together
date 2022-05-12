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
    var parsedQuery = querystring.parse(parsedUrl.path, '/', '=');

    console.log(parsedQuery)
    console.log('첫번째 문자 : ' + parsedQuery["func"])
    response.writeHead(200, { 'Content-Type': 'html/text' });


    if (parsedQuery["func"] == "SearchPlace") { // 장소를 검색하기 위한 쿼리 파싱
        var testQuery = ""
        if (parsedQuery["type"] == "ByName") testQuery = "SELECT * FROM Place WHERE placename like '%" + parsedQuery["str"] + "%'";
        else if (parsedQuery["type"] == "ByAddr") testQuery = "SELECT * FROM Place WHERE addr like '%" + parsedQuery["str"] + "%'";
        else if (parsedQuery["type"] == "ByIds") testQuery = "SELECT * FROM Place WHERE " + parsedQuery["str"];
        else if (parsedQuery["type"] == "Cafe") {
            var locationData = parsedQuery["str"].split(']');
            var myLocationX = locationData[1];
            var myLocationY = locationData[0];
            testQuery = "SELECT *,ST_DISTANCE_SPHERE(POINT(" + myLocationX + "- 90, " + myLocationY + " - 90), POINT(x-90,y-90)) as distance FROM Place WHERE ST_DISTANCE_SPHERE(POINT(" + myLocationX + "- 90, " + myLocationY + " - 90), POINT(x-90,y-90)) <= 1000  AND category = 'Cafe'"
        }
        else if (parsedQuery["type"] == "Convenience") {
            var locationData = parsedQuery["str"].split(']');
            var myLocationX = locationData[1];
            var myLocationY = locationData[0];
            testQuery = "SELECT *,ST_DISTANCE_SPHERE(POINT(" + myLocationX + "- 90, " + myLocationY + " - 90), POINT(x-90,y-90)) as distance FROM Place WHERE ST_DISTANCE_SPHERE(POINT(" + myLocationX + "- 90, " + myLocationY + " - 90), POINT(x-90,y-90)) <= 1000  AND category = 'Convenience'"
        }
        else if (parsedQuery["type"] == "Sleep") {
            var locationData = parsedQuery["str"].split(']');
            var myLocationX = locationData[1];
            var myLocationY = locationData[0];
            testQuery = "SELECT *,ST_DISTANCE_SPHERE(POINT(" + myLocationX + "- 90, " + myLocationY + " - 90), POINT(x-90,y-90)) as distance FROM Place WHERE ST_DISTANCE_SPHERE(POINT(" + myLocationX + "- 90, " + myLocationY + " - 90), POINT(x-90,y-90)) <= 5000  AND category = 'Sleep'"
        }
        else if (parsedQuery["type"] == "Restaurant") {
            var locationData = parsedQuery["str"].split(']');
            var myLocationX = locationData[1];
            var myLocationY = locationData[0];
            testQuery = "SELECT *,ST_DISTANCE_SPHERE(POINT(" + myLocationX + "- 90, " + myLocationY + " - 90), POINT(x-90,y-90)) as distance FROM Place WHERE ST_DISTANCE_SPHERE(POINT(" + myLocationX + "- 90, " + myLocationY + " - 90), POINT(x-90,y-90)) <= 1000  AND category = 'Restaurant'"
        }
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

    if (parsedQuery["func"] == "SearchPost") { // 게시글을 불러오기 위한 쿼리 파싱
        var testQuery = ""
        if (parsedQuery["type"] == "default") testQuery = "SELECT * FROM NOTICEBOARD_DB";
        else if (parsedQuery["type"] == "ByPostName") testQuery = "SELECT * FROM NOTICEBOARD_DB WHERE NOTICEBOARD_TITLE like '%" + parsedQuery["content"] + "%'";
        else if (parsedQuery["type"] == "ByUserName") testQuery = "SELECT * FROM NOTICEBOARD_DB WHERE USER_NAME like '%" + parsedQuery["content"] + "%'";
        else if (parsedQuery["type"] == "ByIds") testQuery = "SELECT * FROM NOTICEBOARD_DB WHERE " + parsedQuery["content"];
        else if (parsedQuery["type"] == "ByTag") testQuery = "SELECT * FROM NOTICEBOARD_DB WHERE LABELS like '%" + parsedQuery["content"] + "%'";
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
    if (parsedQuery["func"] == "PostWrite") {
        var testQuery = "";
        var userdata = parsedQuery["userdata"].split(']');
        var postmain = parsedQuery["postmain"].split(']');
        var user_code = userdata[0];
        var user_name = userdata[1];
        var post_title = postmain[0];
        var post_content = postmain[1];
        var post_date = new Date().toISOString().split('T');
        var course = parsedQuery["coursedata"];
        var tags = parsedQuery["tags"]
        console.log(post_date[1])

        if(parsedQuery["type"] == "default") testQuery = "INSERT INTO NOTICEBOARD_DB (USER_CODE,USER_NAME,RATE,DATES,COURSE,CONTENT,NOTICEBOARD_TITLE,TAGS) VALUES (?,?,?,?,?,?,?,?)";
        var params = [user_code,user_name,'0.0',post_date[0],course,post_content,post_title,tags];
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
    if (parsedQuery["func"] == "BringPostPut") {
        var testQuery = "";
        var postname = parsedQuery["postname"]
        var usercode = parsedQuery["usercode"]
        var course = parsedQuery["coursedata"]
        var percent = parsedQuery["percent"]
        var courseindex = parsedQuery["courseindex"]
        var dayindex = parsedQuery["dayindex"]
        var seq = parsedQuery["seq"]
        var completenum = parsedQuery["coursecurrent"]
        if (parsedQuery["type"] == "default") {
            testQuery = "INSERT INTO UserTravel_DB (POST_NAME,USER_CODE,COURSE,DAY_INDEX,COURSE_INDEX,COMPLETE_PERCENT,COMPLETE_NUM) VALUES (?,?,?,?,?,?,?)";
            var params = [postname, usercode, course, 0, 0, 0, 0];
            connection.query(testQuery, params, function (err, results, fields) { // testQuery 실행
                console.log(err);
                if (err) {
                    console.log(err);
                }
                console.log(results);
                result = JSON.stringify(results);
                console.log("쿼리문 결과 : " + result);
            });
        }
        if (parsedQuery["type"] == "update") {
            testQuery = "UPDATE UserTravel_DB SET DAY_INDEX=?,COURSE_INDEX=?,COMPLETE_PERCENT=?,COMPLETE_NUM=?  WHERE seq = " + seq;
            var params = [dayindex, courseindex, percent, completenum];
            console.log(completenum)
            connection.query(testQuery, params, function (err, results, fields) { // testQuery 실행
                console.log(err);
                if (err) {
                    console.log(err);
                }
                console.log(results);
                result = JSON.stringify(results);
                console.log("쿼리문 결과 : " + result);
            });
        }


    }
    // @정지원 2022-03-26 토 
    // 게시글 가져오기(불러오기)을 위한 로직
    if (parsedQuery["func"] == "BringPostGet") {
        var testQuery = "";
        if(parsedQuery["type"] == "default") testQuery = "SELECT * FROM  UserTravel_DB Where USER_CODE =" + parsedQuery["usercode"];

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
    if (parsedQuery["func"] == "Login") {
        var testQuery = "SELECT * FROM USER_DB WHERE USER_NICKNAME = '" + parsedQuery['id'] + "' and USER_PASS = '" + parsedQuery['password'] + "'"
        var result;
        var ret = {
            "number": "-1",
            "name": "",
            "code": ""
        };

        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }

            if (results.length == 1) {
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
    if (parsedQuery["func"] == "Join") {
        var testQuery = "SELECT * FROM USER_DB WHERE USER_NICKNAME = '" + parsedQuery['id'] + "'"
        var result;
        var flag = 1;
        var ret = {
            "number": "1"
        };

        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
                ret.number = "-2"
            }
            if (results.length >= 1) ret.number = "-1", flag = 0;

            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("아이디 조회 결과 : " + ret);
        });

        if (flag) {
            var testQuery = "INSERT INTO USER_DB VALUES (0, '" + parsedQuery['name'] + "' , '" + parsedQuery['id'] + "' , '"
                + parsedQuery['password'] + "' , '" + parsedQuery['address'] + "' , '" + parsedQuery['gender'] + "' , '" + parsedQuery['taste'] + "')"

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
    if (parsedQuery["func"] == "SearchComment") {
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

    // @솔빈 2022-04-23 토 
    // 이달의 인기 여행지를 추출반환하기 위한 로직
    if (parsedQuery["func"] == "SearchPopularPlace") {
        var testQuery = "SELECT UNIQUE_NUM, COUNT(UNIQUE_NUM) FROM COMMENT_DB WHERE COMMENT_TYPE = 'Place' and DATES >= '"
            + parsedQuery['Start'] + "' and DATES <= '" + parsedQuery['End']
            + "' and COMMENT_RATING >= 4 GROUP BY UNIQUE_NUM HAVING COUNT(UNIQUE_NUM) >= 1 ORDER BY COUNT(UNIQUE_NUM) desc;"


        console.log(testQuery)
        connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
            if (err) console.log(err);

            testQuery = "SELECT * FROM Place WHERE num in (";
            var addedQuery = "ORDER BY FIELD(num,"
            for (var i = 0; i < ret.length; i++) {
                testQuery += ret[i]['UNIQUE_NUM']
                addedQuery += ret[i]['UNIQUE_NUM']
                if (i != ret.length - 1) testQuery += ",", addedQuery += ','
                else testQuery += ")", addedQuery += ");"
            }
            testQuery += addedQuery

            console.log("쿼리문 : " + testQuery);
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
        });
    }


    // @솔빈 2022-04-23 토 
    // 이달의 인기 게시글을 추출 반환하기 위한 로직
    if (parsedQuery["func"] == "SearchPopularPost") {
        var testQuery = "SELECT NOTICEBOARD_NUM, COUNT(LIKE_NUM) FROM LIKE_DB WHERE DATES >= '" + parsedQuery['Start'] + "' and DATES <= '" + parsedQuery['End'] + "' GROUP BY NOTICEBOARD_NUM HAVING COUNT(LIKE_NUM) >= 1 ORDER BY COUNT(LIKE_NUM) DESC;"
        console.log(testQuery)
        connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
            if (err) console.log(err);

            testQuery = "SELECT * FROM NOTICEBOARD_DB WHERE NOTICEBOARD_NUM in (";
            var addedQuery = "ORDER BY FIELD(NOTICEBOARD_NUM,"
            for (var i = 0; i < ret.length; i++) {
                testQuery += ret[i]['NOTICEBOARD_NUM'];
                addedQuery += ret[i]['NOTICEBOARD_NUM'];
                if (i != ret.length - 1) testQuery += ",", addedQuery += ","
                else testQuery += ") ", addedQuery += ");"
            }
            testQuery += addedQuery;

            console.log("쿼리문 : " + testQuery);
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

        });
    }

    //@우람
    //게시글 추천
    if (parsedQuery["func"] == "SearchPostByLike") { // 추천 게시글 로직
       
        var result;
        var getUserTaste = "SELECT USER_CODE, USER_TASTE FROM USER_DB WHERE USER_CODE = '" + parsedQuery["content"] + "'"
        connection.query(getUserTaste, function (err, ret, fields) { 
            if (err) {
                console.log(err);
            }
            
            var getPostLabel = "SELECT NOTICEBOARD_NUM, LABELS FROM NOTICEBOARD_DB"
            connection.query(getPostLabel, function (post_err, labeldata, fields2) {
                if(post_err) {
                    console.log(post_err);
                }
                userTaste = JSON.stringify(ret)
                postLabels = JSON.stringify(labeldata)
                
                var moduleName = './node_test/recommend_module.py'
                var pythonShell = require('python-shell');
                var options = {
                    mode: 'text',
                    pythonPath: '',
                    pythonOptions: ['-u'],
                    scriptPath: '',
                    args: [userTaste, postLabels]
                };

                pythonShell.PythonShell.run(moduleName, options, function (err, results) {
                    if(err) {
                        throw err
                    }
                    var getPostQuery = "SELECT * FROM NOTICEBOARD_DB WHERE NOTICEBOARD_NUM IN ("    
                    var isAllZero = true
                    console.log("result data : ", results[1])
                    datas = JSON.parse(results[1])
                    console.log("parse to json : ", datas["data"])
                    for(var i = 0 ; i < datas["data"][0].length ; i++) {
                        if(datas["data"][0][i] >= 0.5) {
                            getPostQuery += String(datas["columns"][i])+ ","
                            isAllZero = false
                        }
                    }
                    if(isAllZero) {
                        result = ""
                        response.end(result)
                        console.log("쿼리문 결과 : " + result)
                    }

                    else {
                        getPostQuery = getPostQuery.slice(0, -1)
                        getPostQuery += ')'
                        connection.query(getPostQuery, function(errGetPost, post, fields3) {
                            if(errGetPost) {
                                console.log(errGetPost);
                            }

                            result = JSON.stringify(post)
                            response.end(result)
                            console.log("쿼리문 결과 : " + result)
                        })
                    }

                })

                console.log("쿼리문 결과 : " + result)    
            })

           
        });
    }

    // @솔빈 2022-04-16 목 
    // 댓글을 카운팅하기 위한 로직
    if (parsedQuery["func"] == "CountComment") {
        var testQuery = "SELECT count(*) FROM COMMENT_DB WHERE UNIQUE_NUM = '" + parsedQuery['number'] + "' and COMMENT_TYPE = '" + parsedQuery['CommentType'] + "'"
        var result;
        console.log(testQuery)

        connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }
            console.log(ret[0])
            ret = JSON.stringify(ret[0]['count(*)']);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        });
    }

    // @솔빈 2022-03-03 목 
    // 댓글 수정을 위한 로직
    if (parsedQuery["func"] == "EditComment") {
        var testQuery = "UPDATE COMMENT_DB SET CONTENT = '" + parsedQuery['Content'] + "' , DATES = '" + parsedQuery['Dates'] + "', COMMENT_RATING = '" + parsedQuery['Rating'] + "' WHERE COMMENT_NUM = " + parsedQuery['CommentNum']
        var ret = {
            "number": "1"
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
    if (parsedQuery["func"] == "WriteComment") {
        var testQuery = "INSERT INTO COMMENT_DB VALUES (0, " + parsedQuery['UniqueNum'] + " , " + parsedQuery['UserCode'] + " , '"
            + parsedQuery['UserName'] + "' , '" + parsedQuery['Dates'] + "' , '" + parsedQuery['Content'] + "' , '" + parsedQuery['CommentType'] + "' , '" + parsedQuery['Rating'] + "')"
        var ret = {
            "number": "1"
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
    if (parsedQuery["func"] == "DeleteComment") {
        var testQuery = "DELETE FROM COMMENT_DB WHERE COMMENT_NUM = " + parsedQuery['CommentNum']
        var ret = {
            "number": "1"
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
    // @우람 2022-05-08 일
    // 취향이 tag만 불러옴에 따라 약간의 수정.
    if (parsedQuery["func"] == "LoadTaste") {
        var testQuery = "SELECT USER_TASTE FROM USER_DB WHERE USER_CODE = '" + parsedQuery['code'] + "'"
        connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
            if (err) { console.log(err); }
            ret = JSON.stringify(ret[0]);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        });
    }

    //@우람 2022-05-01
    // tag 출력을 위한 tag get
    if (parsedQuery["func"] == "GetTagLabel") {
        var query = "SELECT * FROM TagLabel";

        connection.query(query, function (err, ret, fields) {
            if (err) { console.log(err) }
            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        });
    }

    // @ 솔빈 2022-05-12
    // tag 출력을 위한 tag get
    if(parsedQuery["func"] == "GetTagDict") {
        var query = "";
        if(parsedQuery["type"] == "ByLabel") query = "SELECT * FROM TagDict WHERE labelnum = " + parsedQuery["number"];
        else query = "SELECT * FROM TagDict";

        connection.query(query, function (err, ret, fields) {
            if(err) { console.log(err)}
            if (err) { console.log(err);}
            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        });
    }

    //@우람
    //User Tag Update를 위한 로직
    if (parsedQuery["func"] == "UserTagUpdate") {
        var query = "UPDATE USER_DB SET USER_TASTE = '" + parsedQuery['usertaste'] + "' WHERE USER_CODE = '" + parsedQuery['usercode'] + "'"
        var ret = {
            "number": "1"
        };

        connection.query(query, function (err, result, fields) { // testQuery 실행
            if (err) {
                console.log(err);
                ret.number = "-1"
            }

            ret = JSON.stringify(ret);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        });
    }


    //@우람 2022-05-10
    //usercode를 받아와 해당 유저의 취향 라벨을 불러옴.
    if (parsedQuery["func"] == "GetLabelFromUser") {
        var userTasteQuery = "SELECT USER_TASTE FROM USER_DB WHERE USER_CODE = '" + parsedQuery['code'] + "'";
        var taste = "";

        connection.query(userTasteQuery, function (err, result, fields) { // userTasteQuery 실행
            if (err) {
                console.log(err);
            }

            taste = JSON.stringify(result).split(":")[1].replace('}', '').replace(']', '').replaceAll('"', '').replace(' ', '')
            taste = taste.split(',')

            var returnTasteLabel = "SELECT * FROM TagLabel WHERE "

            for (var iter in taste) {
                returnTasteLabel += "num = '" + (parseInt(taste[iter]) + 1) + "' "
                if (taste[iter] == taste[taste.length - 1]) {
                    continue;
                }
                returnTasteLabel += "OR "
            }
            var ret;
            connection.query(returnTasteLabel, function (err, result, fields) { // userTasteQuery 실행
                if (err) {
                    console.log(err);
                }

                ret = JSON.stringify(result)
                response.end(ret);
                console.log("쿼리문 결과 : " + ret);
            });

        });

    }

    // @솔빈 2022-04-23 일 
    // 좋아요 갯수를 불러오기 위한 로직
    if (parsedQuery["func"] == "GetLikeCnt") {
        var testQuery = "SELECT count(*) FROM LIKE_DB WHERE NOTICEBOARD_NUM = " + parsedQuery['NoticeNum'] + ""
        connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
            if (err) console.log(err);
            console.log(ret[0])
            ret = JSON.stringify(ret[0]['count(*)']);
            response.end(ret);
            console.log("쿼리문 결과 : " + ret);
        });
    }

    // @솔빈 2022-04-23 일 
    // 좋아요 DB에 추가하기 위한 로직
    if (parsedQuery["func"] == "AddLike") {
        var testQuery = "SELECT count(*) FROM LIKE_DB WHERE NOTICEBOARD_NUM = " + parsedQuery['NoticeNum'] + " and USER_CODE = " + parsedQuery['UserCode']
        var returnvalue = ""
        console.log("쿼리문 : " + testQuery);
        connection.query(testQuery, function (err, results, fields) { // DB에 좋아요가 존재하는지 여부를 먼저 검사
            if (err) console.log(err);
            results = JSON.stringify(results[0]['count(*)'])
            returnvalue = results

            if (returnvalue == "0") { // DB에 좋아요가 없는 경우
                var testQuery = "INSERT INTO LIKE_DB VALUES (0, " + parsedQuery['NoticeNum'] + " , " + parsedQuery['UserCode'] + " , '" + parsedQuery['Dates'] + "')"
                connection.query(testQuery, function (err, results, fields) { // testQuery 실행
                    if (err) {
                        console.log(err);
                    }
                    results = JSON.stringify(results);
                    response.end("1");
                });
            }

            else { // DB에 좋아요가 있는 경우
                // 좋아요 DB에서 삭제하기 위한 로직
                var testQuery = "DELETE FROM LIKE_DB WHERE NOTICEBOARD_NUM = " + parsedQuery['NoticeNum'] + " and USER_CODE = " + parsedQuery['UserCode']
                connection.query(testQuery, function (err, ret, fields) { // testQuery 실행
                    if (err) {
                        console.log(err);
                        ret.number = "-1"
                    }
                    ret = JSON.stringify(ret);
                    response.end("-1");
                });
            }
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
		cb(null, './ProfileImages')
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
    if (type == "ProfileImages") filepath += '_Profile.jpeg'
    else if (type == "PlaceImages") filepath += '.jpeg'

    fs.readFile(filepath, function (err, data) {
        console.log(filepath);
        console.log(data);
        res.end(JSON.stringify(data));
    });
});


app.listen(port, () => {
    console.log(`server is listening at localhost:${port}`);
});