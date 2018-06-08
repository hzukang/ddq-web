/**
 * New node file
 */

var app = require('../app.js');
var queues= require('mysql-queues');
var async=require('async');

/*
 *加钱操作
 *
 */
exports.upMoney=function(uid,cash,timestamp)
{
	var result={};
	queues(app.conn);
	var trans = app.conn.startTransaction();
	async.series([
	              function(insert_cb) { 
	                  var insert_sql = "INSERT INTO `tb_money` (`cash`,`uid`,`createdate`) VALUES (`"+app.conn.escape(cash)+"`,`"+uid+"`,`"+timestamp+"`) where exists";
	                  // 执行第一条sql语句 如果出错 直接进入最后的 错误方法 回滚事务
	                  trans.query(insert_sql, function(err, info) {
	                	  insert_cb(err, info);
	                  })
	                  
	              },
	              function(update_cb_1) { 
	            	  
	                  var update_sql_1 = "UPDATE `tb_money` SET `cash`=cash+"+app.conn.escape(cash)+" WHERE `uid`='"+uid+"'";
	                  // 执行第二条sql语句 如果出错 直接进入最后的 错误方法 回滚事务
	                  trans.query(update_sql_1, function(err, info) {
	                      update_cb_1(err, info);
	                  })
	              },
	              function(update_cb_2) {
	            	  
	                  var update_sql_2 = "UPDATE `test` SET `name`='管理会社  2' WHERE `id`='16'";
	                  // 执行第三条sql语句 如果出错 直接进入最后的 错误方法 回滚事务
	                  trans.query(update_sql_2, function(err, info) {
	                      update_cb_2(err, info);
	                  })
	              }
	          ], function(err, results) {
		          
	              if (err) {
	            	  result.errcode="-1"
	                  result.errmsg=err;
	                  result.data={};
	                  // 出错的场合 回滚
	                  trans.rollback();
	              } else {
	            	  result.errcode="0"
		              result.errmsg="";
		              result.data={};
	                  // 没有错误的场合 提交事务
	            	  trans.commit();
	              }
	              
	              return result;
	          });
	          // 执行这个事务
	          trans.execute();
}

//exports.downMoney=function()
