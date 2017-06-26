<%--
  Created by IntelliJ IDEA.
  User: kobe_xuan
  Date: 2017/6/1
  Time: 23:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="statics/images/googleg_lodp.ico">
    <title>Google</title>
    <style>
        *{
            margin: 0px;
            padding: 0px;
        }
        a{
            text-decoration: none;
            font-family: arial,sans-serif;
        }
        .nav{
            margin-top: 15px;
            display: flex;
            flex-direction: row-reverse;
            padding: 0px 30px;
        }
        .gb_Ha{
            border: 1px solid #4285f4;
            font-weight: bold;
            outline: none;
            background: #4285f4;
            background: -webkit-linear-gradient(top,#4387fd,#4683ea);
            color: white;
            display: inline-block;
            line-height: 28px;
            padding: 0 12px;
            border-radius: 2px;
            font-size: 13px;
        }
        .gb_b{
            color: #000;
            background-position: -132px -38px;
            opacity: .55;
            background-image:url('//ssl.gstatic.com/gb/images/i1_1967ca6a.png');
            background-size: 528px 68px;
            display: inline-block;
            outline: none;
            vertical-align: middle;
            border-radius: 2px;
            box-sizing: border-box;
            height: 30px;
            width: 30px;
            margin-right: 15px;
        }
        .gb_b:hover{
            opacity: .85;
        }
        .gb_p{
            color: black;
            display: inline-block;
            line-height: 24px;
            outline: none;
            vertical-align: middle;
            opacity: .75;
            font-size: 13px;
            padding-top: 4px;
            margin-right: 15px;
        }
        .gb_p:hover{
            text-decoration: underline;
            opacity: .85;
        }
        .container{
            position: absolute;
            top: 0px;
            left: 0px;
            text-align: center;
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            flex-direction: column;
            justify-content: center;
        }
        .input_div{
            margin-top: 20px;
            margin-left: auto;
            margin-right: auto;
            padding:5px 10px;
            width: 40%;
            background-color: #fff;
            height: 34px;
            vertical-align: top;
            border-radius: 2px;
            box-shadow: 0 2px 2px 0 rgba(0,0,0,0.16), 0 0 0 1px rgba(0,0,0,0.08);
            transition: box-shadow 200ms cubic-bezier(0.4, 0.0, 0.2, 1);
        }
        .input_div input{
            padding-right: 44px;
            width: 100%;
            box-sizing: border-box;
            height: 100%;
            border: none;
            font-size:16px;
        }
        .input_div input:focus{
            outline: none;
        }
        .input_div .mc_icon{
            text-align: center;
            width: 24px;
            padding: 0 8px;
            display: inline-block;
            position: absolute;
            height: 44px;
            right: 5px;
            top: 0px;
            padding-top: 10px;
        }
        .input_div .mc_icon span{
            width: 18px;
            display: inline-block;
            height: 23px;
            vertical-align: middle;
            background: url("<%=basePath%>statics/images/mc.png") no-repeat -3px 0px;
        }
        button{
            height: 36px;
            line-height: 27px;
            background-color: #f2f2f2;
            border: 1px solid #f2f2f2;
            border-radius: 2px;
            color: #757575;
            cursor: default;
            font-family: arial,sans-serif;
            font-size: 13px;
            font-weight: bold;
            margin: 11px 4px;
            min-width: 54px;
            padding: 0 16px;
            text-align: center;
        }
        button:hover{
            background-image: -webkit-linear-gradient(top,#f8f8f8,#f1f1f1);
            border: 1px solid #c6c6c6;
            box-shadow: 0 1px 1px rgba(0,0,0,0.1);
            color: #222;
            background-color: #f8f8f8;
        }
        .dialog{
            background: white;
            position: absolute;
            top: 0px;
            left: 0px;
            height: 100%;
            width: 100%;
            z-index: 99;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .dialog .content{
            padding-left: 100px;
            padding-right: 100px;
            width: 70%;
        }
    </style>
</head>
<body>
<!--语音识别弹框-->
<div id="mydialog" class="dialog" style="display: none">
    <a id="cancel_btn" href="#" style="position: absolute;right: 20px;top: 15px;">X</a>
    <div class="content">
        <p id="recongnize_msg" style="font-size: 25px;display: inline-block;margin-top: 90px;">请开始说话</p>
        <img src="<%=basePath%>statics/images/big_mc.png" alt="" style="float: right">
    </div>
</div>
<!--顶部导航栏-->
<div class="nav">
    <a class="gb_Ha" href="#">登录</a>
    <a class="gb_b" href="#"></a>
    <a class="gb_p" href="#">图片</a>
    <a class="gb_p" href="#">Gmail</a>
</div>
<!--主体部分-->
<div class="container">
    <div style="display: flex;flex-direction: column;width: 100%;margin-top: -5%;">
        <div class="logo" style="background-size: 272px 92px;height: 92px;width: 272px;background: url('<%=basePath%>statics/images/googlelogo_color.png');margin: auto;">

        </div>
        <div class="input_div" style="position: relative">
            <form id="myForm" style="height: 100%;" action="result.html">
                <input id="search_input" type="text" name="key">
                <a id="mc_icon" class="mc_icon" href="#">
                <span style="background-size: 24px 24px;">

                </span>
                </a>
            </form>
        </div>
        <div class="button_div" style="margin-top: 18px;">
            <button>Google 搜索</button>
            <button>手气不错</button>
        </div>
    </div>
</div>
</body>
<script>
    window.onload=function () {
        var mc_icon=document.getElementById("mc_icon");
        var mydialog=document.getElementById("mydialog");
        var cancel_btn=document.getElementById("cancel_btn");
        var search_input=document.getElementById("search_input");
        var myForm=document.getElementById("myForm");
        var recongnize_msg=document.getElementById("recongnize_msg");
        var SpeechRecognition = webkitSpeechRecognition || SpeechRecognition;
        var recog = new SpeechRecognition();//初始化录音对象
        recog.lang = 'zh-CN';
        search_input.onkeydown=function(event){
            if(event.keyCode== 13&&search_input.value!=""){
                console.log(search_input.value);
                return true;
            }
            else if(event.keyCode==13&&search_input.value==""){
                alert('输入不能为空');
                return false;
            }

        };

        mc_icon.onclick=function(){
            mydialog.style.display="flex";
            startSpeech();
        };

        mydialog.onclick=function () {
            this.style.display="none";
        };

        cancel_btn.onclick=function () {
            mydialog.style.display="none";
        };
        //开始录音
        function startSpeech(){
            recog.start();
        }
        recog.onaudiostart = function (e) {
            console.log('start');
        };
        recog.onspeechend= function (e) {
            console.log('speech end');
            recongnize_msg.innerHTML="正在识别中，请确保您的网络能访问外网"

        };
        recog.onend = function() {

        };
        recog.onaudioend = function (e) {
            console.log('onaudioend');

        };
        recog.onstop = function (e) {
            console.log('on stop');

        };
        recog.onresult = function (e) {
            var last = event.results.length - 1;
            console.log("onresult"+event.results[last][0].transcript);
            search_input.setAttribute("value",event.results[last][0].transcript);
            mydialog.style.display="none";
            myForm.submit();
        };
        recog.onerror = function (e) {
            //window.alert("Recognition error :" + e.error);
            console.log("error");
            console.log(e);
            recongnize_msg.innerHTML="语音识别失败，请确认您的网络能访问外网"
        }
    }
</script>
</html>
