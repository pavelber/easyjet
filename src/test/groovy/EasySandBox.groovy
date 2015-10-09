import groovy.json.JsonSlurper

def data = new URL('http://www.easyjet.com/EN/linkedAirportsJSON').getText()
def s = data.split("\n")[0]
String d = s.substring(12, s.length()-2)
def jsonSlurper = new JsonSlurper()
println jsonSlurper.parseText(d)