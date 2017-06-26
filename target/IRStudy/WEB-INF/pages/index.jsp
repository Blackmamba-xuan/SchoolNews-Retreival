<%--
  Created by IntelliJ IDEA.
  User: kobe_xuan
  Date: 2017/6/4
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width">
    <title>Speech color changer</title>
    <!--[if lt IE 9]>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <style>
        body, html {
            margin: 0;
        }

        html {
            height: 100%;
        }

        body {
            height: inherit;
            overflow: hidden;
        }

        h1, p {
            font-family: sans-serif;
            text-align: center;
            padding: 20px;
        }

        div {
            height: 100px;
            overflow: auto;
            position: absolute;
            bottom: 0px;
            right: 0;
            left: 0;
            background-color: rgba(255,255,255,0.2);
        }

        ul {
            margin: 0;
        }

        .hints span {
            text-shadow: 0px 0px 6px rgba(255,255,255,0.7);
        }

    </style>
</head>

<body>
<h1>Speech color changer</h1>

<p class="hints"></p>
<div>
    <p class="output"><em>...diagnostic messages</em></p>
</div>

<script>
    var SpeechRecognition = SpeechRecognition || webkitSpeechRecognition
    var SpeechGrammarList = SpeechGrammarList || webkitSpeechGrammarList
    var SpeechRecognitionEvent = SpeechRecognitionEvent || webkitSpeechRecognitionEvent

    var colors = [ 'aqua' , 'azure' , 'beige', 'bisque', 'black', 'blue', 'brown', 'chocolate', 'coral', 'crimson', 'cyan', 'fuchsia', 'ghostwhite', 'gold', 'goldenrod', 'gray', 'green', 'indigo', 'ivory', 'khaki', 'lavender', 'lime', 'linen', 'magenta', 'maroon', 'moccasin', 'navy', 'olive', 'orange', 'orchid', 'peru', 'pink', 'plum', 'purple', 'red', 'salmon', 'sienna', 'silver', 'snow', 'tan', 'teal', 'thistle', 'tomato', 'turquoise', 'violet', 'white', 'yellow'];
    var grammar = '#JSGF V1.0; grammar colors; public <color> = ' + colors.join(' | ') + ' ;'

    var recognition = new SpeechRecognition();
    var speechRecognitionList = new SpeechGrammarList();
    speechRecognitionList.addFromString(grammar, 1);
    recognition.grammars = speechRecognitionList;
    //recognition.continuous = false;
    recognition.lang = 'en-US';
    recognition.interimResults = false;
    recognition.maxAlternatives = 1;

    var diagnostic = document.querySelector('.output');
    var bg = document.querySelector('html');
    var hints = document.querySelector('.hints');

    var colorHTML= '';
    colors.forEach(function(v, i, a){
        console.log(v, i);
        colorHTML += '<span style="background-color:' + v + ';"> ' + v + ' </span>';
    });
    hints.innerHTML = 'Tap/click then say a color to change the background color of the app. Try '+ colorHTML + '.';

    document.body.onclick = function() {
        recognition.start();
        console.log('Ready to receive a color command.');
    }

    recognition.onresult = function(event) {
        // The SpeechRecognitionEvent results property returns a SpeechRecognitionResultList object
        // The SpeechRecognitionResultList object contains SpeechRecognitionResult objects.
        // It has a getter so it can be accessed like an array
        // The [last] returns the SpeechRecognitionResult at the last position.
        // Each SpeechRecognitionResult object contains SpeechRecognitionAlternative objects that contain individual results.
        // These also have getters so they can be accessed like arrays.
        // The [0] returns the SpeechRecognitionAlternative at position 0.
        // We then return the transcript property of the SpeechRecognitionAlternative object

        var last = event.results.length - 1;
        var color = event.results[last][0].transcript;

        diagnostic.textContent = 'Result received: ' + color + '.';
        bg.style.backgroundColor = color;
        console.log('Confidence: ' + event.results[0][0].confidence);
    }

    recognition.onspeechend = function() {
        recognition.stop();
    }

    recognition.onnomatch = function(event) {
        diagnostic.textContent = "I didn't recognise that color.";
    }

    recognition.onerror = function(event) {
        diagnostic.textContent = 'Error occurred in recognition: ' + event.error;
    }

</script>
</body>
</html>
