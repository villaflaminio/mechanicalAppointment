<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <style>
        /* Add styles to make the email look nicer */
        body {
            font-family: Arial, sans-serif;
            font-size: 16px;
            line-height: 1.5;
            color: #333333;
            margin: 0;
            padding: 0;
        }

        h1 {
            margin-top: 0;
            margin-bottom: 24px;
            font-size: 24px;
            font-weight: bold;
            color: #333333;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            margin-bottom: 24px;
        }

        table td,
        table th {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        table th {
            background-color: #f2f2f2;
        }

        p {
            margin-bottom: 16px;
        }
    </style>
</head>

<body>
<h1>Appointment Summary</h1>
<p>Dear ${customerName},</p>
<p>We are pleased to inform you that your appointment has been completed successfully. The details of the appointment are as follows:</p>

<table>
    <tr>
        <th>Appointment Detail</th>
        <th>Information</th>
    </tr>
    <tr>
        <td>Comment:</td>
        <td>${comment}</td>
    </tr>
    <tr>
        <td>Estimated Time:</td>
        <td>${appointmentTime}</td>
    </tr>
    <tr>
        <td>Price:</td>
        <td>${price}</td>
    </tr>
    <tr>
        <td>Mechanical Action:</td>
        <td>${mechanicalAction}</td>
    </tr>
</table>

<p>Thank you for choosing our services. We appreciate your business.</p>

<p>We hope you are satisfied with the service you received. If you have any questions or concerns, please don't hesitate to contact us at ${contactEmail}.</p>

<p>Sincerely,</p>
<p>The Mechanical Manager Team</p>
</body>
</html>