<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<html>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<style>
form {
    border: 3px solid #f1f1f1;
    width: 557px;
    margin-left: 27%;
}

input[type=text], input[type=password] {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
}

button {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    width: 100%;
}

button:hover {
    opacity: 0.8;
}

.cancelbtn {
    width: auto;
    padding: 10px 18px;
    background-color: #f44336;
}

.imgcontainer {
    text-align: center;
    margin: 24px 0 12px 0;
}

img.avatar {
    width: 40%;
    border-radius: 50%;
}

.container {
    padding: 16px;
    width: 500px;
    margin-left: 1%;
    margin-bottom: 26px;
}

span.psw {
    float: right;
    padding-top: 16px;
}
.form_header{
margin-left:41%;
display:block;
}

/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 300px) {
    span.psw {
       display: block;
       float: none;
    }
    .cancelbtn {
       width: 100%;
    }
}

scroll{
	overflow:scroll; 
	width:40px;
	height:10px;
}
</style>
<body>

<h2 class="form_header">Login Form</h2>

<form method="post" action="${contextPath}/web/userWeb/login">
  <!-- div style="width: 200px; height: 100px; overflow-y: scroll; border: solid;">
    test<br />
    test<br />
    test<br />
    test<br />
    test<br />
    test<br />
    test<br />
    test<br />
    test<br />
    test<br />
    </div-->
  <div class="container">
    <label><b>Username</b></label>
    <input type="text" placeholder="Enter Username" name="uname" required>

    <label><b>Password</b></label>
    <input type="password" placeholder="Enter Password" name="psw" required>
        
    <button type="submit">Login</button>
    <input type="checkbox" checked="checked"> Remember me
  </div>

  <div class="container" >
    <span class="psw" >Forgot <a href="#">password?</a></span>
  </div>
</form>

</body>
</html>
