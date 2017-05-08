import React from 'react'

import {
  View,
  Text,
  Button,
  NativeAppEventEmitter,
} from 'react-native'

import RNPush from 'react-native-notification'

export default class App extends React.Component {
  constructor() {
    super()
    this.state = {
      registrationId: 'loading',
    }
  }

  async componentDidMount() {
    RNPush.init()

    RNPush.getRegistrationId()
      .then((data) => {
        this.setState(data)
      })

    NativeAppEventEmitter.addListener('getRegistrationId', (data) => {
      this.setState(data)
    })
	}

  componentWillUnmount() {
    NativeAppEventEmitter.removeAllListeners()
  }

  handleClearPress = async () => {
    await RNPush.clearAllNotifications()
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>{this.state.type || 'push service type'}</Text>
				<Text selectable={true}>{this.state.registrationId || 'loading'}</Text>
        <View>
          <Button title="Clear All Notification" onPress={this.handleClearPress} />
        </View>
      </View>
    )
  }
}

const styles = {
  container: {
    flex: 1,
    padding: 20,
  },
}
