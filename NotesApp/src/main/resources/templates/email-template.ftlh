<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f2f2f2;
    }

    .container {
      max-width: 600px;
      margin: 0 auto;
      padding: 20px;
      background-color: #ffffff;
      border-radius: 10px;
      box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
    }

    h2 {
      color: #333333;
      margin-top: 20px;
      text-align: center;
    }

    p {
      color: #555555;
      font-size: 16px;
      line-height: 1.5;
      margin-bottom: 10px;
      text-align: center;
    }

    .instructions {
      background-color: #f9f9f9;
      border-radius: 5px;
      padding: 10px;
    }

    .button {
      display: inline-block;
      margin-top: 20px;
      padding: 10px 20px;
      background-color: #000000;
      color: #ffffff;
      text-decoration: none;
      border-radius: 5px;
      text-align: center;
      cursor: pointer;
    }

    .button:hover {
      background-color: #333333;
    }

    .center {
      text-align: center;
    }

    .footer {
      margin-top: 20px;
      text-align: center;
      color: #999999;
    }
  </style>
</head>
<body>
<div class="container">
  <h2>Password Recovery</h2>
  <p>Hello <span style="font-weight: bold;">${username}</span>,</p>
  <p>We received a request to recover your password.</p>
  <div class="instructions">
    <p>Your login: <span style="font-weight: bold;">${login}</span></p>
    <p>Your password: <span style="font-weight: bold;">${password}</span></p>
  </div>
  <p>If you didn't request this, please ignore this email.</p>
  <div class="center">
    <a class="button" href="http://localhost:8080/login">Login Now</a>
  </div>
  <div class="footer">
    This email was sent by Notes Minder. If you have any questions, please contact us.
  </div>
</div>
</body>
</html>
