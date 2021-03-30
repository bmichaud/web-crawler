def USER_HOME = System.getProperty("user.home")
println "USER_HOME=${USER_HOME}"
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}
appender("File", RollingFileAppender) {
    println "Setting [file] property to [${USER_HOME}/logs/web-crawler.log]"
    file = "${USER_HOME}/logs/web-crawler.log"
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = "${USER_HOME}/logs/web-crawler-%d{yyyy-MM-dd}.i.log"
        maxFileSize = "100MB"
        maxHistory = 30
        totalSizeCap = "1GB"
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}
logger("net.benmichaud.webcrawler", WARN, ["File"])
root(WARN, ["STDOUT"])
