import {
  NativeModules,
  Platform,
	NativeAppEventEmitter,
} from 'react-native'

// import PushNotificationIOS from '@react-native-community/push-notification-ios'


let registrationIdIOS = ''
// PushNotificationIOS.addEventListener('register', (token) => {
// 	registrationIdIOS = token
// })

const RNPush = {
  addEventListener: (event, handler) => {
    // if (event === 'register') {
    //   return PushNotificationIOS.addEventListener('register', (token) => {
	  //     registrationIdIOS = token
    //     handler({
    //       type: 'Apple',
    //       registrationId: token,
    //     })
    //   })
    // } else {
    //   PushNotificationIOS.addEventListener(event, handler)
    // }
  },
  getRegistrationId: () => {
		// return Promise.resolve({
    //   type: 'Apple',
    //   registrationId: registrationIdIOS,
    // })
  },
  clearBadge: () => {
    // PushNotificationIOS.setApplicationIconBadgeNumber(0)
  },
	init: () => {
    // have to call this to get fresh registrationId
    // PushNotificationIOS.requestPermissions()
  },
	stop: () => {},
	resume: () => {},
	removeAllNotifications: () => {
		// PushNotificationIOS.removeAllDeliveredNotifications()
	},
	removeNotifications: (id) => {
		// PushNotificationIOS.removeDeliveredNotifications(id)
	}
}

export default RNPush
