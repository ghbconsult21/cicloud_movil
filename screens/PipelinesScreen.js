import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  TextInput,
} from 'react-native';
import PipelineCard from '../components/PipelineCard';

const ALL_PIPELINES = [
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
  {
    id: '5',
    name: 'build-android',
    project: 'cicloud-movil',
    status: 'success',
    branch: 'main',
    duration: '5m 10s',
    timestamp: 'hace 1h',
  },
  {
    id: '6',
    name: 'deploy-production',
    project: 'cicloud-web',
    status: 'failed',
    branch: 'release/2.0',
    duration: '3m 22s',
    timestamp: 'hace 2h',
  },
  {
    id: '7',
    name: 'unit-tests',
    project: 'cicloud-api',
    status: 'running',
    branch: 'develop',
    duration: '1m 55s',
    timestamp: 'hace 2h',
  },
  {
    id: '8',
    name: 'code-analysis',
    project: 'cicloud-web',
    status: 'success',
    branch: 'main',
    duration: '4m 01s',
    timestamp: 'hace 3h',
  },
];

const FILTERS = ['Todos', 'Activos', 'Fallidos', 'En cola'];

export default function PipelinesScreen() {
  const [search, setSearch] = React.useState('');
  const [activeFilter, setActiveFilter] = React.useState('Todos');

  const filtered = ALL_PIPELINES.filter((p) => {
    const matchesSearch =
      p.name.toLowerCase().includes(search.toLowerCase()) ||
      p.project.toLowerCase().includes(search.toLowerCase());

    const matchesFilter =
      activeFilter === 'Todos' ||
      (activeFilter === 'Activos' && p.status === 'running') ||
      (activeFilter === 'Fallidos' && p.status === 'failed') ||
      (activeFilter === 'En cola' && p.status === 'queued');

    return matchesSearch && matchesFilter;
  });

  return (
    <View style={styles.container}>
      <View style={styles.searchBar}>
        <TextInput
          style={styles.input}
          placeholder="Buscar pipeline o proyecto..."
          placeholderTextColor="#999"
          value={search}
          onChangeText={setSearch}
        />
      </View>

      <View style={styles.filterRow}>
        {FILTERS.map((filter) => (
          <TouchableOpacity
            key={filter}
            style={[styles.filterBtn, activeFilter === filter && styles.filterBtnActive]}
            onPress={() => setActiveFilter(filter)}
          >
            <Text style={[styles.filterText, activeFilter === filter && styles.filterTextActive]}>
              {filter}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      <FlatList
        data={filtered}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => <PipelineCard pipeline={item} />}
        contentContainerStyle={styles.list}
        ListEmptyComponent={
          <View style={styles.empty}>
            <Text style={styles.emptyText}>No se encontraron pipelines</Text>
          </View>
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F0F4F8',
  },
  searchBar: {
    backgroundColor: '#fff',
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  input: {
    backgroundColor: '#F5F5F5',
    borderRadius: 8,
    paddingHorizontal: 14,
    paddingVertical: 8,
    fontSize: 14,
    color: '#333',
  },
  filterRow: {
    flexDirection: 'row',
    paddingHorizontal: 12,
    paddingVertical: 10,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  filterBtn: {
    paddingHorizontal: 14,
    paddingVertical: 6,
    borderRadius: 16,
    marginHorizontal: 4,
    backgroundColor: '#F0F4F8',
  },
  filterBtnActive: {
    backgroundColor: '#0078D4',
  },
  filterText: {
    fontSize: 13,
    color: '#555',
  },
  filterTextActive: {
    color: '#fff',
    fontWeight: '600',
  },
  list: {
    paddingHorizontal: 16,
    paddingTop: 12,
    paddingBottom: 24,
  },
  empty: {
    alignItems: 'center',
    paddingTop: 40,
  },
  emptyText: {
    color: '#999',
    fontSize: 15,
  },
});
