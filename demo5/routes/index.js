
/*
 * GET home page.
 */
var url = require('url');
var qs = require('querystring');
var crypto = require('crypto');
var app = require('../app.js');
var queues= require('mysql-queues');
var async=require('async');


exports.index = function(req, res){
	/*var postData = JSON.stringify({
		  'msg' : 'Hello World!'
		}); 
	var options = {host:'www.google.com',port:80, path:'/upload', method: 'post', headers: {  
        "Content-Type": 'application/x-www-form-urlencoded',  
        "Content-Length":postData.length 
    }  };
	var req1=http.request(options,function(serverback){
		 console.log('STATUS: ' + serverback.statusCode);
		 console.log('HEADERS: ' + serverback.headers);
		 serverback.setEncoding('utf8');
		 serverback.on('data', function(chunk){
			 console.log('BODY: ' + chunk);
		    });
		 serverback.on('error', function(err){
			 console.log('RESPONSE ERROR: ' + err);
		 });
	})
	req1.on('error', function(err){
		console.log(
		'REQUEST ERROR: ' + err);
    });*/
	
	//app.logger.error(app.config.connection);
	//app.logger.error(app.config.connection);
	//app.logger.error("ffff");
	queues(app.conn);
	var trans = app.conn.startTransaction();
	async.series([
	              function(insert_cb) { 
	                  var insert_sql = "INSERT INTO `test` (`name`) VALUES ('可立即订j购hhhh')";
	                  // 执行第一条sql语句 如果出错 直接进入最后的 错误方法 回滚事务
	                  trans.query(insert_sql, function(err, info) {
	                	  console.log("ui:"+info.insertId)
	                	  insert_cb(err, info);
	                  })
	                  console.log("1");
	              },
	              function(update_cb_1) { 
	            	  console.log("2");
	                  var update_sql_1 = "INSERT INTO `test` (`id`,`name`) VALUES ('26','可立即订j购hhhh')";
	                  
	                  // 执行第二条sql语句 如果出错 直接进入最后的 错误方法 回滚事务
	                  trans.query(update_sql_1, function(err, info) {
	                      update_cb_1(err, info);
	                  })
	              },
	              function(update_cb_2) {
	            	  console.log("3");
	                  var update_sql_2 = "UPDATE `test` SET `name`='管理会社  2' WHERE `id`='16'";
	                  // 执行第三条sql语句 如果出错 直接进入最后的 错误方法 回滚事务
	                  trans.query(update_sql_2, function(err, info) {
	                      update_cb_2(err, info);
	                  })
	              }
	          ], function(err, results) {
		           console.log("4");
	              if (err) {
	            	  console.log(err);
	                  console.log("rollback");
	                  // 出错的场合 回滚
	                  trans.rollback();
	              } else {
	            	  console.log(results);
	                  // 没有错误的场合 提交事务
	                  trans.commit();
	              }
	              
	          });
	          // 执行这个事务
	          trans.execute();
	
	res.render('index', { title: 'index' });
	//res.end();
};

exports.test=function(req,res)
{
	/*var htmlparser = require("htmlparser2");
	var parser = new htmlparser.Parser({
	    onopentag: function(name, attribs){
	    	 console.log(JSON.stringify(attribs));
	        if(name === "script" && attribs.type === "text/javascript"){
	            console.log("JS! Hooray!");
	        }
	    },
	    ontext: function(text){
	        console.log("-->", text);
	    },
	    onclosetag: function(tagname){
	    	 console.log(tagname);
	        if(tagname === "script"){
	            console.log("That's it?!");
	        }
	    }
	}, {decodeEntities: true});
	parser.write("<xml> <ToUserName><![CDATA[khs]]></ToUserName> <FromUserName><![CDATA[khs]]></FromUserName> <CreateTime>1359044526</CreateTime>"+
    "<MsgType><![CDATA[test]]></MsgType> <Content><![CDATA[2015-04-18 11:12:00]]></Content> <FuncFlag>0</FuncFlag></xml>");
	parser.end();
	
	var htmlparser = require("htmlparser");
	var rawHtml = "<xml> <ToUserName><![CDATA[khs]]></ToUserName> <FromUserName><![CDATA[khs]]></FromUserName> <CreateTime>1359044526</CreateTime>"+
    "<MsgType><![CDATA[test]]></MsgType> <Content><![CDATA[2015-04-18 11:12:00]]></Content> <FuncFlag>0</FuncFlag></xml>";
	var handler = new htmlparser.DefaultHandler(function (error, dom) {
	    if (error)
	        console.log(error)
	    else
	    	console.log(dom.children)
	}, { verbose: false, ignoreWhitespace: true });
	var parser = new htmlparser.Parser(handler);
	parser.parseComplete(rawHtml);
	console.log(JSON.stringify(handler.dom));*/
	
	var parseString = require('xml2js').parseString;
	var xml = "<xml> <ToUserName><![CDATA[khs]]></ToUserName> <FromUserName><![CDATA[khs]]></FromUserName> <CreateTime>1359044526</CreateTime>"+
    "<MsgType><![CDATA[test]]></MsgType> <Content><![CDATA[2015-04-18 11:12:00]]></Content> <FuncFlag>0</FuncFlag></xml>";
	parseString(xml,{ explicitArray : false, ignoreAttrs : true }, function (err, result) {
	    console.log(result.xml.ToUserName);
	});
	
	res.end();
}

exports.check = function(req,res)
{
	 var getQuery = url.parse(req.url).query;
	 var getData  = qs.parse(getQuery); //getData数据 
	 //$token, $timestamp, $nonce
	 var signature=getData.signature;
	 var echostr = getData.echostr;
	 var oriArray = new Array();  
	    oriArray[0] = getData.nonce;  
	    oriArray[1] = getData.timestamp;  
	    oriArray[2] = "app1013112247"; 
	    oriArray.sort();  
	 var tmpstr=oriArray[0]+oriArray[1]+oriArray[2];
	 var md5sum = crypto.createHash('sha1');
	 md5sum.update(tmpstr,'utf8');
	 tmpstr = md5sum.digest('hex');
	
	 var postData = ""; 
	 req.addListener("data", function (postDataChunk) {
	        postData += postDataChunk;
	 });
	 req.addListener("end", function () {
	        var params = qs.parse(postData);
	        
	 });
	 req.addListener("end", function () {

		   	var parseString = require('xml2js').parseString;
		    if(postData!=null&&postData!="")
		    {
		     parseString(postData,{ explicitArray : false, ignoreAttrs : true }, function (err, result)
		     {
			    console.log(result.xml.ToUserName);
		      var textTpl="";
		      console.log("postData:"+postData);  
			    if(tmpstr==signature)
		      {
		        textTpl="<xml> <ToUserName><![CDATA["+result.xml.FromUserName+"]]></ToUserName> <FromUserName><![CDATA["+result.xml.ToUserName+"]]></FromUserName> <CreateTime>"+result.xml.CreateTime+"</CreateTime>"+
		               "<MsgType><![CDATA[text]]></MsgType> <Content><![CDATA["+new Date()+"]]></Content> <FuncFlag>0</FuncFlag></xml>";
		        res.send(textTpl);

		      }else
		      {
		        res.send();
		      }
		     });
		   }
		   });
	 
};

exports.login = function(req, res){
	res.render('login', { title: '用户登陆'});
};

exports.doLogin = function(req, res){
	var user={
	username:'admin',
	password:'admin'
	}
	if(req.body.username===user.username && req.body.password===user.password)
	{
	  req.session.user=user;
	  res.redirect('/home');
	}
	else{ req.session.error='用户名或密码不正确';res.redirect('/login');console.log( req.session.error+" "+req.body.password); return;}
	
};

exports.logout = function(req, res){
	req.session.user=null;
	res.redirect('/');
};

exports.home = function(req, res){
	var user={
	username:'admin',
	password:'admin'
	}
	res.render('home', { title: 'Home'});
};