<%--
  Created by IntelliJ IDEA.
  User: kobe_xuan
  Date: 2017/6/2
  Time: 10:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>搜索结果-${key}</title>
    <style>
        *{
            margin: 0px;
            padding: 0px;
        }
        a{
            text-decoration: none;
            font-family: arial,sans-serif;
            color: #1a0dab;
            font-weight: normal;
        }
        .nav{
            padding: 20px 15px;
            background: #fafafa;
            padding-bottom: 48px;
        }
        .input_div{
            padding:5px 10px;
            width: 40%;
            background-color: #fff;
            height: 34px;
            vertical-align: top;
            border-radius: 2px;
            box-shadow: 0 2px 2px 0 rgba(0,0,0,0.16), 0 0 0 1px rgba(0,0,0,0.08);
            transition: box-shadow 200ms cubic-bezier(0.4, 0.0, 0.2, 1);
            display: inline-block;
            margin-left: 15px;
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
            position: relative;
            padding-left: 155px;
            margin: auto;
        }
        .container_nav{
            display: block;
            position: relative;
            list-style: none;
            top: -38px;
        }
        ul.container_nav li{
            display: inline-block;
        }
        ul.container_nav li a{
            color: #777;
            font-size: 13px;
            padding-bottom: 12px;
            padding-left: 16px;
            padding-right: 16px;
        }
        ul.container_nav li a.active{
            border-bottom: 3px solid #4285f4;
            color: #4285f4;
            font-weight: bold;
        }
        ul.container_nav li a.active:hover{
            color: #4285f4;
        }
        ul.container_nav li a:hover{
            color: #222;
        }
        .result_state{
            color: #808080;
            font-size: 13px;
            position: relative;
            top: -10px;
        }
        .g{
            margin-bottom: 26px;
            font-weight: normal;
            width: 47%;
        }
        .g cite{
            display: block;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .g cite a{
            font-size: 14px;
            color: #006621;
            font-style: normal;
            line-height: 16px;
            white-space: nowrap;
        }
        .g a:hover{
            text-decoration: underline;
        }
        .g .anchor{
            font-size: 13px;
            line-height:1.54;
            max-height: 78px;
        }
        .footer{
            height: 65px;
            padding-top: 8px;
            background-color: #F2F2F2;
            position: relative;
            bottom: 0px;
            width: 100%;
        }
        .footer{
            padding-left: 155px;
            padding-right: 155px;
            box-sizing: border-box;
        }
        .msg{
            margin: auto auto;
        }
        .unknown_loc{
            border-radius: 100%;
            display: inline-block;
            height: 10px;
            margin: 6px 4px 9px 0;
            vertical-align: middle;
            width: 10px;
            background: #9e9e9e;
        }
        .swml_addr{
            color: #aaa;
            font-size: 13px;
        }
        .fab a{
            color: #777;
            font-size: 13px;
            padding-right: 27px;
        }
        .navcnt{
            /* margin: auto auto;*/
            width: 50%;
        }
        .navcnt table{
            border-collapse:collapse;
            text-align:left;
            margin: 30px auto 30px;
        }
        .navcnt td{
            padding: 0;
            text-align: center;
        }
        .cur{
            display: block;
            color: black;
        }
        .cur a{
            color: #000;
            display: block;
        }
        .cur span{
            background: url("<%=basePath%>statics/images/nav_logo242.png") no-repeat;
            background-position: -53px 0;
            width: 20px;
            height: 40px;
            display: block;
            color: rgba(0,0,0,0.87);
            font-weight: normal;
            text-align: center;
        }
        .fl{
            color: #4285f4;
            font-weight: normal;
            white-space: nowrap;
            display: block;
        }
        .ch{
            background: url("<%=basePath%>statics/images/nav_logo242.png") no-repeat;
            background-position: -74px 0;
            width: 20px;
            display: block;
            height: 40px;
            margin-bottom: 3px;
        }
        .pn{
            color: #4285f4;
            font-weight: normal;
            text-align: left;
        }
        .csend{
            background: url("<%=basePath%>statics/images/nav_logo242.png") no-repeat;
            background-position: -96px 0;
            width: 71px;
            height: 40px;
            display: block;
            font-size: 14px;
        }
        .subscript{
            color: #666;
            font-size: 13px;
            margin-right: 20px;
        }
        .g img{
            width:18px;
            height: 18px;
        }
    </style>
</head>
<body>
<!--下面是顶部导航栏-->
<div class="nav">
    <img src="<%=basePath%>statics/images/googlelogo_color_120x44dp.png" alt="" style="height: 44px;width: 120px;">
    <div class="input_div" style="position: relative">
        <form style="height: 100%" action="result.html">
            <input id="search_input" type="text" name="key" value="${key}">
            <a id="mc_icon" class="mc_icon" href="#">
                <span style="background-size: 24px 24px;">

                </span>
            </a>
        </form>

    </div>
    <div style="float: right;">
        <a class="gb_p" href="#">Gmail</a>
        <a class="gb_p" href="#">图片</a>
        <a class="gb_b" href="#"></a>
        <a class="gb_Ha" href="#">登录</a>
    </div>
</div>
<!--下面是内容部分-->
<div class="container">
    <ul class="container_nav">
        <li><a class="nav-a active" href="javascript:;">新闻</a></li>
        <li><a class="nav-a" href="javascript:;">附件</a></li>
        <li><a class="nav-a" href="javascript:;">图片</a></li>
        <li><a class="nav-a" href="#">视频</a></li>
        <li><a class="nav-a" href="#">地图</a></li>
        <li><a class="nav-a" href="#">更多</a></li>
    </ul>
    <div class="result_state">
        找到约${length}条结果 （用时 ${time} 毫秒）
    </div>
    <div id="result_div" class="result">
         <c:forEach items="${docList }" var="result" end="9">
             <div class="g">
                 <h3><a href="${result.link}">${result.title}</a></h3>
                 <cite><a href="${result.link}">${result.link}</a></cite>
                 <div class="anchor">
                  ${result.anchor1}<span style="color:red;">${key}</span>${result.anchor2}
                 </div>
                 <span class="subscript">发文时间：${result.time}</span><span class="subscript">发文单位：${result.unit}</span><span class="subscript">点击数：${result.clickNum}</span>
             </div>
         </c:forEach>

    </div>
    <div class="navcnt">
        <table>
            <tbody>
            <tr>
                <td><span style="background:url(<%=basePath%>statics/images/nav_logo242.png) no-repeat;background-position:-24px 0;width:28px;height: 40px;display: block;margin-top: -18px;"></span></td>
                <td class="cur"><a class="fl" href="javascript:;"><span class="ch"></span>1</a></td>
                <c:forEach var="i" begin="2" end="${pageNum}" varStatus="status">
                    <td><a class="fl" href="javascript:;" index="${status.index}"><span class="ch"></span>${status.index}</a></td>
                </c:forEach>
                <td class="navend">
                    <a class="pn" href="#">
                        <span class="csend"></span>
                        <span style="display: block;margin-left: 53px;">下一页</span>
                    </a>
                </td>
            </tr>
            </tbody>

        </table>
    </div>
</div>

<!--下面是底部部分-->
<div class="footer">
    <div class="msg">
        <span class="unknown_loc"></span>
        <span class="swml_addr">南山区 广东省深圳市- 基于您的 IP 地址</span>
        <div class="fab">
            <a href="#">帮助</a>
            <a href="#">发送反馈</a>
            <a href="#">隐私权</a>
            <a href="#">条款</a>
        </div>
    </div>
</div>
</body>
<script>

    window.onload=function(){
        var search_input=document.getElementById("search_input");
        var result=<%= session.getAttribute("result") %>;
        var fileResult=<%= session.getAttribute("fileResult")%>
        var fl=document.getElementsByClassName("fl");
        var result_div=document.getElementById("result_div");
        console.log(result.length);
        var nav_a=document.getElementsByClassName("nav-a");
        console.log(nav_a.length)
        for(var i=0;i<nav_a.length;i++){
            console.log("点击了"+i);
            //点击了新闻
            if(i==0){
                nav_a[i].onclick=function () {
                    var active=document.getElementsByClassName("nav-a active")[0];
                    active.setAttribute("class","nav-a");
                    this.setAttribute("class","nav-a active");
                    var html="";
                    console.log(i);
                    for(var j=0;j<10;j++){
                        var doc=result[j];
                        console.log(doc['link']);
                        html+="<div class='g'>"+'<h3><a href="'+doc['link']+'">'+doc['title']+'</a></h3>';
                        html+='<cite><a href="'+doc['link']+'">'+''+doc['link']+'</a></cite>';
                        html+=' <div class="anchor">'+doc['anchor1']+'<span style="color: red;">'+doc['key']+'</span>'+doc['anchor2']+' </div>';
                        html+='<span class="subscript">发文时间：'+doc['time']+'</span><span class="subscript">发文单位：'+doc['unit']+'</span><span class="subscript">点击数：'+doc['clickNum']+'</span></div>';
                    }
                    result_div.innerHTML=html;
                    return false;
                }
            }
            //点击了附件
            else if(i==1){
                console.log("点击了附件");
                nav_a[i].onclick=function () {
                    var active=document.getElementsByClassName("nav-a active")[0];
                    active.setAttribute("class","nav-a");
                    this.setAttribute("class","nav-a active");
                    var html="";
                    console.log(i);
                    for(var j=0;j<10;j++){
                        var doc=fileResult[j];
                        console.log(doc['link']);
                        html+="<div class='g'>"+'<h3><img src="<%=basePath%>statics/images/paper_clip_icon.png"><a href="'+doc['link']+'">'+doc['title']+'</a></h3>';
                        html+='<cite><a href="'+doc['link']+'">'+''+doc['link']+'</a></cite>';
                        html+=' <div class="anchor">'+doc['anchor1']+'<span style="color: red;">'+doc['key']+'</span>'+doc['anchor2']+' </div>';
                        html+='<span class="subscript">发文时间：'+doc['time']+'</span><span class="subscript">发文单位：'+doc['unit']+'</span><span class="subscript">点击数：'+doc['clickNum']+'</span></div>';
                    }
                    result_div.innerHTML=html;
                    return false;
                }
            }
        }
        search_input.onkeydown=function(event){
            if(event.keyCode== 13&&search_input.value!=""){
                console.log(search_input.value);
                return true;
            }
            else if(event.keyCode==13&&search_input.value==""){
                alert('输入不能为空');
                return false;
            }

        }
        for(var i=0;i<fl.length;i++){
            fl[i].onclick=function(){
                console.log(this.getAttribute("index"));
                var index=this.getAttribute("index");
                var html="";
                if(index*10+10>result.length){
                    for(var j=index*10;j<result.length;j++){
                        var doc=result[j];
                        console.log(result[j]);
                        html+="<div class='g'>"+'<h3><a href="'+doc.link+'">'+doc.title+'</a></h3>';
                        html+='<cite><a href="'+doc.link+'">'+''+doc.link+'</a></cite>';
                        html+=' <div class="anchor">'+doc['anchor1']+'<span style="color: red;">'+doc['key']+'</span>'+doc['anchor2']+'</div>';
                        html+='<span class="subscript">发文时间：'+doc['time']+'</span><span class="subscript">发文单位：'+doc['unit']+'</span><span class="subscript">点击数：'+doc['clickNum']+'</span></div>';
                    }
                }
                else{
                    for(var j=index*10;j<(10+index*10);j++){
                        var doc=result[j];
                        console.log(doc['link']);
                        html+="<div class='g'>"+'<h3><a href="'+doc['link']+'">'+doc['title']+'</a></h3>';
                        html+='<cite><a href="'+doc['link']+'">'+''+doc['link']+'</a></cite>';
                        html+=' <div class="anchor">'+doc['anchor1']+'<span style="color: red;">'+doc['key']+'</span>'+doc['anchor2']+' </div>';
                        html+='<span class="subscript">发文时间：'+doc['time']+'</span><span class="subscript">发文单位：'+doc['unit']+'</span><span class="subscript">点击数：'+doc['clickNum']+'</span></div>';
                    }
                }
                result_div.innerHTML=html;
                var cur=document.getElementsByClassName("cur")[0];
                cur.setAttribute("class","fl");
                this.setAttribute("class","cur");
                return false;
            }
        }
    }
</script>
</html>
