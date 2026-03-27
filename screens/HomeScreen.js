import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  RefreshControl,
} from 'react-native';
import PipelineCard from '../components/PipelineCard';

const MOCK_STATS = [
  { label: 'Activos', value: '12', color: '#4CAF50' },
  { label: 'Fallidos', value: '3', color: '#F44336' },
  { label: 'En Cola', value: '5', color: '#FF9800' },
];

const RECENT_PIPELINES = [
  {
    id: '1',
    name: 'build-production',
    project: 'cicloud-api',
    status: 'success',
    branch: 'main',
    duration: '2m 34s',
    timestamp: 'hace 5 min',
  },
  {
    id: '2',
    name: 'deploy-staging',
    project: 'cicloud-web',
    status: 'running',
    branch: 'develop',
    duration: '1m 12s',
    timestamp: 'hace 10 min',
  },
  {
    id: '3',
    name: 'test-suite',
    project: 'cicloud-movil',
    status: 'failed',
    branch: 'feature/login',
    duration: '45s',
    timestamp: 'hace 20 min',
  },
  {
    id: '4',
    name: 'lint-check',
    project: 'cicloud-api',
    status: 'success',
    branch: 'hotfix/auth',
    duration: '30s',
    timestamp: 'hace 35 min',
  },
];

export default function HomeScreen() {
  const [refreshing, setRefreshing] = React.useState(false);

  const onRefresh = React.useCallback(() => {
    setRefreshing(true);
    setTimeout(() => setRefreshing(false), 1500);
  }, []);

  return (
    <ScrollView
      style={styles.container}
      refreshControl={
        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor="#0078D4" />
      }
    >
      <View style={styles.header}>
        <Text style={styles.greeting}>Bienvenido a CI Cloud</Text>
        <Text style={styles.subtitle}>Panel de control</Text>
      </View>

      <View style={styles.statsRow}>
        {MOCK_STATS.map((stat) => (
          <View key={stat.label} style={[styles.statCard, { borderTopColor: stat.color }]}>
            <Text style={[styles.statValue, { color: stat.color }]}>{stat.value}</Text>
            <Text style={styles.statLabel}>{stat.label}</Text>
          </View>
        ))}
      </View>

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Pipelines Recientes</Text>
        {RECENT_PIPELINES.map((pipeline) => (
          <PipelineCard key={pipeline.id} pipeline={pipeline} />
        ))}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F0F4F8',
  },
  header: {
    backgroundColor: '#0078D4',
    padding: 20,
    paddingBottom: 30,
  },
  greeting: {
    color: '#fff',
    fontSize: 22,
    fontWeight: 'bold',
  },
  subtitle: {
    color: '#B3D9F7',
    fontSize: 14,
    marginTop: 4,
  },
  statsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    marginTop: -16,
    marginBottom: 16,
  },
  statCard: {
    flex: 1,
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 14,
    alignItems: 'center',
    marginHorizontal: 4,
    borderTopWidth: 3,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  statValue: {
    fontSize: 24,
    fontWeight: 'bold',
  },
  statLabel: {
    fontSize: 12,
    color: '#666',
    marginTop: 2,
  },
  section: {
    paddingHorizontal: 16,
    marginBottom: 24,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
    marginBottom: 10,
  },
});
