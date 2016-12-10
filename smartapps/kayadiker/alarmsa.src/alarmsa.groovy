
definition(
    name: "alarmSA",
    namespace: "kayadiker",
    author: "mali",
    description: "desc",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Title") {
		input "alarmDH", "device.alarmDH"
	}
    
    section("Turn on this light") {
        input "theswitch", "capability.switch", required: false, multiple: true
    }
    section( "Notifications" ) {
    		input"recipients", "contact", title: "Send notifications to", required: false
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
	subscribe( alarmDH, "alarmStatus", handleAlarm )
	subscribe( alarmDH, "healthStatus", healthCheck )
	//runEvery5Minutes(healthCheckEveryXmin)
	state.healthCheckedAt = alarmDH.currentValue("healthStatus");
	state.healthNotified = false;
}

def handleAlarm(evt) {
    if (alarmDH.currentValue("alarmStatus") == "panic") {
    	theswitch.on()
 		if (sendPushMessage=="Yes") { 
        	sendPush( "Alarm triggered!!" )
           }
 		//sendSms( phone, "Alarm triggered!!" )
	} else if (alarmDH.currentValue("alarmStatus") == "disarmed") {
    	theswitch.off()
        
 		if (sendPushMessage=="Yes") { 
 			sendPush( "Security disarmed!!" )
           }
	} else if (alarmDH.currentValue("alarmStatus") == "armed") {
    	theswitch.off()
        
 		if (sendPushMessage=="Yes") { 
 			sendPush( "Security armed!!" )
           }
	}
}
def healthCheck(evt) {
	state.healthCheckedAt = now()  
 		//sendPush( "12.8secs")  
}
def healthCheckEveryXmin(evt) {
	//sendPush( "Last check-in: ${state.healthCheckedAt}")
	//sendPush( "Now: ${now()}") 
	sendPush( "Idle for: ${now()-state.healthCheckedAt}") 
	sendPush( "Left for health fail: ${15*60000-(now()-state.healthCheckedAt)}") 
	//state.healthCheckedAt2 = state.healthCheckedAt
    //alarmDH.healthStatus2= now()-state.healthCheckedAt; bu olmadi
    if((now()-state.healthCheckedAt) > 15*60000) 
    	{
        if(state.healthNotified == false)
        	{
 			sendPush( "Health check failed!" )            
			state.healthNotified = true;
            }
    	} 
    else
    	{    
		state.healthNotified = false;
    	}
    
}
