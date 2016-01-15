import groovy.json.JsonSlurper

def data = new URL('http://www.easyjet.com/EN/linkedAirportsJSON').getText()
def jsonSlurper = new JsonSlurper()
List<String> list = jsonSlurper.parseText(data.substring(12, data.length() - 2).replace('\'', '"'))
String str = list.findAll { it.startsWith("TLV") }

println str