import {
  NativeModules,
  Platform,
  PushNotificationIOS,
	NativeAppEventEmitter,
} from 'react-native'

let registrationIdIOS = ''
PushNotificationIOS.addEventListener('register', (token) => {
	registrationIdIOS = token
})

const RNPush = {
  addEventListener: (event, handler) => {
    if (event === 'register') {
      return PushNotificationIOS.addEventListener('register', (token) => {
	      registrationIdIOS = token
        handler({
          type: 'Apple',
          registrationId: token,
        })
      })
    } else {
      PushNotificationIOS.addEventListener(event, handler)
    }
  },
  getRegistrationId: () => {
		return Promise.resolve({
      type: 'Apple',
      registrationId: registrationIdIOS,
    })
  },
  clearBadge: () => {
    PushNotificationIOS.setApplicationIconBadgeNumber(0)
  },
	init: () => {
    // have to call this to get fresh registrationId
    // PushNotificationIOS.requestPermissions()
  },
	stop: () => {},
	resume: () => {},
	clearAllNotifications: () => {
		PushNotificationIOS.removeAllDeliveredNotifications()
	},
	clearNotificationById: (id) => {
		PushNotificationIOS.removeDeliveredNotifications(id)
	}
}

export default RNPush
