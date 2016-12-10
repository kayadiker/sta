metadata {
  // Automatically generated. Make future change here.
  definition (name: "alarmDH", namespace: "kayadiker", author: "mali") {
    capability "Alarm"
    capability "Switch"

    attribute "alarmStatus", "string"
    attribute "healthStatus", "number"
    attribute "healthStatus2", "number"

    command "arm"
    command "disarm"
    command "clear"
    command "panic"
  }

  // UI tile definitions
  tiles {
    standardTile("alarmStatus", "device.alarmStatus", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false) {
      state "disarmed", label: 'Off', action: "arm", icon: "st.Home.home2", backgroundColor: "#ffffff"
      state "armed", label: 'On', action: "disarm", icon: "st.Home.home3", backgroundColor: "#00ff00"
    	state "panic", label: 'Alarm', action: "clear", icon: "st.Home.home2", backgroundColor: "#ff0000"
    }
    standardTile("panic", "device.panic", width: 1, height: 1, canChangeIcon: false, canChangeBackground: true) {
      state "panic", label:'Panic', action:"panic", icon:"st.alarm.alarm.alarm", backgroundColor:"#ff0000"
    }
    standardTile("healthStatus", "device.healthStatus", wordWrap: true, width: 2, height: 1) {
        state("healthStatus", label:'${currentValue}', unit:"", backgroundColor:"#ffffff")
    }
    standardTile("healthStatus2", "device.healthStatus2", wordWrap: true, width: 2, height: 1) {
        state("healthStatus2", label:'${currentValue}', unit:"", backgroundColor:"#ffffff")
    }
    main(["alarmStatus"])
    details(["alarmStatus","panic","healthStatus","healthStatus2"])
  }
}

// Parse incoming device messages to generate events
def parse(String description) {
  log.debug description
  def msg = zigbee.parse(description)?.text
  log.debug "Received ${msg}"
  def result = []
  /*if (!msg || msg == "ping") {
    result += createEvent(name: null, value: msg)
  } */
    if (msg == "healhty") {
    result += createEvent(name: "healthStatus", value: now())
  } 
if ( msg == "disarmed" ) {  
        result += createEvent(name: "alarmStatus", value: "disarmed")
      }
if ( msg == "armed" ) {  
        result += createEvent(name: "alarmStatus", value: "armed")
      }
if ( msg == "panic" ) {  
        result += createEvent(name: "alarmStatus", value: "panic")
      }
/*if (msg?.endsWith("health")) {

	def parts = msg.text.split( /:/ );

        result += createEvent(name: "time1", value: "abs")
  //o butonun şuanki değeri msg dan büyük mü?
  //büyükse o değeri msg ile dğeiştir
  //değilse alarmmmm
  }
*/
  log.debug "Parse returned:"
  return result
}

// Implement "switch" (turn alarm on/off)
def on() {
  arm()
}

def off() {
  disarm()
}

// Commands sent to the device
def arm() {
  log.debug "Sending arm command"
  zigbee.smartShield(text: "arm").format()
}


def disarm() {
  log.debug "Sending disarm command"
  zigbee.smartShield(text: "disarm").format()
}

def strobe() {
  arm()
}

def siren() {
  panic()
}

def both() {
  panic()
}


def panic() {
  log.debug "Sending panic command"
  zigbee.smartShield(text: "panic").format()
}

// TODO: Need to send off, on, off with a few secs in between to stop and clear the alarm
def clear() {
  arm()
}

def update() {
  log.debug "Sending update command"
  zigbee.smartShield(text: "update").format()
}

def configure() {
  update()
}