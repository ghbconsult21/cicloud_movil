import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';

const STATUS_CONFIG = {
  success: { color: '#4CAF50', label: 'Exitoso', icon: '✓' },
  failed: { color: '#F44336', label: 'Fallido', icon: '✗' },
  running: { color: '#FF9800', label: 'En ejecución', icon: '▶' },
  queued: { color: '#9E9E9E', label: 'En cola', icon: '⏳' },
  cancelled: { color: '#607D8B', label: 'Cancelado', icon: '⊘' },
};

export default function PipelineCard({ pipeline, onPress }) {
  const status = STATUS_CONFIG[pipeline.status] || STATUS_CONFIG.queued;

  return (
    <TouchableOpacity style={styles.card} onPress={onPress} activeOpacity={0.75}>
      <View style={[styles.statusBar, { backgroundColor: status.color }]} />
      <View style={styles.content}>
        <View style={styles.topRow}>
          <Text style={styles.pipelineName} numberOfLines={1}>
            {pipeline.name}
          </Text>
          <View style={[styles.badge, { backgroundColor: status.color + '22' }]}>
            <Text style={[styles.badgeText, { color: status.color }]}>
              {status.icon} {status.label}
            </Text>
          </View>
        </View>
        <Text style={styles.project}>{pipeline.project}</Text>
        <View style={styles.bottomRow}>
          <Text style={styles.meta}>🌿 {pipeline.branch}</Text>
          <Text style={styles.meta}>⏱ {pipeline.duration}</Text>
          <Text style={styles.meta}>{pipeline.timestamp}</Text>
        </View>
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  card: {
    flexDirection: 'row',
    backgroundColor: '#fff',
    borderRadius: 10,
    marginBottom: 10,
    overflow: 'hidden',
    elevation: 1,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.08,
    shadowRadius: 2,
  },
  statusBar: {
    width: 4,
  },
  content: {
    flex: 1,
    padding: 12,
  },
  topRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 4,
  },
  pipelineName: {
    fontSize: 15,
    fontWeight: '600',
    color: '#222',
    flex: 1,
    marginRight: 8,
  },
  badge: {
    borderRadius: 10,
    paddingHorizontal: 8,
    paddingVertical: 2,
  },
  badgeText: {
    fontSize: 11,
    fontWeight: '600',
  },
  project: {
    fontSize: 12,
    color: '#888',
    marginBottom: 8,
  },
  bottomRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  meta: {
    fontSize: 11,
    color: '#999',
  },
});
