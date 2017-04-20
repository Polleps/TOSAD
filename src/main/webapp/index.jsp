<!DOCTYPE html>
<head>
    <title>TOSAD JAVA SERVER OUTPUT</title>
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
</head>
<style>
    body {
        background-color: #c9f5ff;
        font-family: 'Roboto', sans-serif;
        padding: 0px;
        margin: 0px;
    }
    header {
        background-color: #0286a3;
        margin: 0px;
        padding:0px;
    }
    h1{
        color: white;
        margin: 0px;
        padding: 10px;
    }
    #controllerConsole {
        background-color: white;
        height: 90%;
        overflow-y: scroll;;
        margin: 10px;
        height: 800px;
    }
    #consoleText {
        margin: 10px;
    }
    .requestHead {
        font-size: 19px;
        color: #0794a3;
        text-decoration: underline;
    }
</style>
<body style="">
<header>
    <H1>JAVA DEBUGGING</H1>
    <script>
        var xhttp  = new XMLHttpRequest();
        xhttp.open("GET", "http://tosad2-polleps.rhcloud.com/restservices/fetch/getTableData/{url:'ondora02.hu.nl:8521',userName:'tosad_2016_2d_team6',password:'tosad_2016_2d_team6',dbId:'27',serviceName:'cursus02.hu.nl'}", false);
        xhttp.send();
        'http://tosad2-polleps.rhcloud.com/restservices/generator/deleteRule/{url:"'||v('DATABASE_URL')||'",serviceName:"'||v('DATABASE_SERVICE_NAME')||'",userName:"tosad_2016_2d_team6",password:"tosad_2016_2d_team6",dbId:"'|| rule_target ||'",ruleName:"' || rule_name || '",ruleTable:"' || rule_table ||'", ruleTypeId:"' || rule_type_id ||'"}'

    </script>
</header>
<div id="controllerConsole">
    <p id="consoleText">
        <%String output = Domain.Controller.getOut();%>
        <%=output%>
    </p>
</div>
</body>
