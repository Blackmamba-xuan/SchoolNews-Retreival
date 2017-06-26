/**
 * Created by kobe_xuan on 2017/6/4.
 */
var SpeechRecognition = webkitSpeechRecognition || SpeechRecognition;
var recog = new SpeechRecognition();//初始化录音对象
recog.lang = 'zh-CN';

