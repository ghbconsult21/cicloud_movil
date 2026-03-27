import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { StatusBar } from 'expo-status-bar';
import { Text } from 'react-native';

import HomeScreen from './screens/HomeScreen';
import PipelinesScreen from './screens/PipelinesScreen';
import SettingsScreen from './screens/SettingsScreen';

const Tab = createBottomTabNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <StatusBar style="light" />
      <Tab.Navigator
        screenOptions={({ route }) => ({
          tabBarIcon: ({ focused, color }) => {
            let icon;
            if (route.name === 'Inicio') {
              icon = focused ? '🏠' : '🏡';
            } else if (route.name === 'Pipelines') {
              icon = focused ? '🔄' : '🔧';
            } else if (route.name === 'Ajustes') {
              icon = focused ? '⚙️' : '🔩';
            }
            return <Text style={{ fontSize: 20 }}>{icon}</Text>;
          },
          tabBarActiveTintColor: '#0078D4',
          tabBarInactiveTintColor: '#888',
          tabBarStyle: {
            backgroundColor: '#1E1E2E',
            borderTopColor: '#333',
          },
          tabBarLabelStyle: {
            fontSize: 12,
          },
          headerStyle: {
            backgroundColor: '#0078D4',
          },
          headerTintColor: '#fff',
          headerTitleStyle: {
            fontWeight: 'bold',
          },
        })}
      >
        <Tab.Screen name="Inicio" component={HomeScreen} options={{ title: 'CI Cloud' }} />
        <Tab.Screen name="Pipelines" component={PipelinesScreen} options={{ title: 'Pipelines' }} />
        <Tab.Screen name="Ajustes" component={SettingsScreen} options={{ title: 'Ajustes' }} />
      </Tab.Navigator>
    </NavigationContainer>
  );
}
