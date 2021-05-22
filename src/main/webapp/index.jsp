<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>DAS - Login</title>

    <!-- Font Icon -->
    <link rel="stylesheet" href="resources/assets/fonts/material-icon/css/material-design-iconic-font.min.css">

    <!-- Main css -->
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body>
    <div class="main">
        <!-- Sign in  Form -->
        <section class="sign-in">
            <div class="container">
                <div class="signin-content">
                    <div class="signin-image">
                        <figure><img src="resources/assets/images/doc.png" alt="sign up image"></figure>
                        <a href="register.jsp" class="signup-image-link">Create an account</a>
                        <!--a href="#" class="signup-image-link">Login as Guest</a-->
                    </div>

                    <div class="signin-form">
                        <h2 class="form-title">Login</h2>
                        <form method="post" class="register-form" id="login-form" action="<%= request.getContextPath() %>/login">
                            <div class="form-group">
                                <label for="your_name"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                <input type="email" name="email" id="your_name" placeholder="Email"/>
                            </div>
                            <div class="form-group">
                                <label for="your_pass"><i class="zmdi zmdi-lock"></i></label>
                                <input type="password" name="password" id="your_pass" placeholder="Password"/>
                            </div>
                            <!--div class="form-group">
                                <input type="checkbox" name="remember-me" id="remember-me" class="agree-term" />
                                <label for="remember-me" class="label-agree-term"><span><span></span></span>Remember me</label>
                                <a href="#" class="signup-image-link" style="display: flex; ">Forgot password</a>
                            </div-->
                            <div class="form-group form-button">
                                <input type="submit" name="signin" id="signin" class="form-submit" value="Log in"/>
                            </div>
                        </form>
                        
                    </div>
                </div>
            </div>
        </section>

    </div>

    <!-- JS -->
    <script src="resources/vendor/jquery/jquery.min.js"></script>
</body>
</html>