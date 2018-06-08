
/**
 * Module dependencies.
 */


var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , index= require('./routes/index')
  , http = require('http')
  , path = require('path'),
  ejs = require('ejs'),
  fs=require('fs'),
  mysql=require('mysql'),
  tanjin=require('tenjin'),
  session = require('express-session');
var connection;//数据库链接
var app =  express();

var config=JSON.parse(fs.readFileSync('./package.json',encoding='utf8',function(err){if(err){
	console.log(err);
	return false; 
	}}));

var log4js = require('log4js');
log4js.configure({
  appenders: [
    { type: 'console' }, //控制台输出
    { type: 'file',
      filename: 'logs/ERR.log', 
      maxLogSize: 10240000,
      backups:3,
      category: 'ERR'}
  ]
});


var logger = log4js.getLogger('ERR');
logger.setLevel('INFO');

app.configure(function(){
	 
  app.set('port', process.env.PORT || 3000);
  app.set('views', __dirname + '/views');
  
  //app.engine('.html', ejs.renderFile);  
  app.set('view engine', 'ejs'); 
  
  
  app.use(express.favicon());
  app.use(express.logger('dev'));
  app.use(express.json());
  app.use(express.urlencoded());
  
  app.use(express.methodOverride());
  
  //session注册
  //app.use(express.cookieParser('keyboard cat'));
  //app.use(express.session({ secret: 'keyboard cat', key: 'sid', cookie: { secure: true }}));
  //app.use(express.session());
  app.use(express.cookieParser('keyboard cat'));
  app.use(session({
	  resave: false, // don't save session if unmodified
	  saveUninitialized: false, // don't create session until something stored
	  secret: 'keyboard cat'
	}));
  

  app.use(function(req,res,next)
		  {
	         res.locals.user = req.session.user;
	         var err = req.session.error;
	         delete req.session.error;
	         res.locals.message = '';
	         if (err) res.locals.message = '<div class="alert alert-error">' + err + '</div>';
	         next();
		  });
  //this.logger('normal'), {level:'auto', format:':method :url'})
  app.use(log4js.connectLogger(logger, {level:log4js.levels.INFO, format:':method :url'}));
  app.use(app.router);
  app.use(express.static(path.join(__dirname, 'public')));
  
 });

app.configure('development', function(){
  app.use(express.errorHandler());
});



function IsLogin(req,res,next)
{
	if (!req.session.user) {
		req.session.error='请先登陆';
		return res.redirect('/login');
	}
	next();
}


var htmlTemplete = {
		html:{},
		loadTemplete:function (path) {
		this.html['content']={};
		this.html['content'].string= fs.readFileSync(path, encoding = 'utf8');
		this.html['content'].template = new tenjin.Template();
		this.html['content'].template.convert(this.html['content'].string);
		}
	};


function GetConn()
{
	connection = mysql.createConnection(config.connection);
	connection.connect(function(err)
			{
		        if(err)
		        {
	            console.log("err:"+err+" time:"+new Date());
	            setTimeout(GetConn,2000);
	            return;
		        }
		        console.log("success");
	        }
	);
	connection.on("error",function(err)
			{ 
		        console.log("err:"+err+" time:"+new Date());
		        if(err.code=="PROTOCOL_CONNECTION_LOST")
		        {
		        	GetConn();
		        }else
		        {
		        	throw err;
		        }
			})
}

GetConn();
exports.conn=connection;
exports.config=config;
exports.logger=logger;

app.get('/',routes.index);
app.get('/login', index.login);
app.post('/login',index.doLogin);
app.get('/logout',index.logout);
app.get('/home',IsLogin,index.home);
app.get('/check',index.check);
app.post('/check',index.check);
app.get('/test',index.test);

http.createServer(app).listen(app.get('port'), function(){
   console.log("Express server listening on port " + app.get('port'));
});
