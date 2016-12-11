definition(
    name: "alarmcancelSA",
    namespace: "kayadiker",
    author: "mali",
    description: "desc",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Which sensor") {
		input "thecontactsensor", "capability.contactSensor", required: true
	}
    
    section("Silence alarm") {
        input "theswitch", "capability.switch", required: true
    }
    section( "Notifications" ) {
    		//input "recipients", "contact", title: "Send notifications to", required: false
            input "sendPushMessage", "enum", title: "Send a push notification?", options: ["Yes", "No"], required: false
            input "phone", "phone", title: "Send A Text Message?", required: false
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
    
}

def initialize() {
	subscribe( thecontactsensor, "acceleration", handleVibration )
	subscribe( theswitch, "alarmStatus", handleAlarm )
	//runEvery5Minutes(healthCheckEveryXmin)
	state.alarmDurum = theswitch.currentValue("alarmStatus")
	//state.healthNotified = false;
}

def handleAlarm(evt) {
	state.alarmDurum = theswitch.currentValue("alarmStatus")
}

def handleVibration(evt) {
    if (thecontactsensor.currentValue("acceleration") == "active") {
    
    log.debug  theswitch.currentValue("alarmStatus")
    
    if ( theswitch.currentValue("alarmStatus") == "panic") {
    	theswitch.off()
    	/*theswitch.off()
    	theswitch.on()
    	theswitch.off()
    	theswitch.on()
    	theswitch.off()
    	theswitch.on()*/
 		if (sendPushMessage=="Yes") { 
        	sendPush( "Alarm susturuldu" )
           }
        if (phone) {
 			sendSms( phone, "Alarm susturuldu" )
           }
	} else { 
    	 if (sendPushMessage=="Yes") { 
        	sendPush( "Zil caldi..." )
           }
    }
    
    }
}