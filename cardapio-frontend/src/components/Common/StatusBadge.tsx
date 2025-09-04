import React from 'react';
import { PedidoStatus } from '../../types';

interface StatusBadgeProps {
  status: PedidoStatus;
  className?: string;
}

const StatusBadge: React.FC<StatusBadgeProps> = ({ status, className = '' }) => {
  const getStatusConfig = () => {
    switch (status) {
      case PedidoStatus.PENDENTE:
        return {
          label: 'Pendente',
          className: 'bg-yellow-100 text-yellow-800 border-yellow-200',
        };
      case PedidoStatus.CONFIRMADO:
        return {
          label: 'Confirmado',
          className: 'bg-blue-100 text-blue-800 border-blue-200',
        };
      case PedidoStatus.EM_PREPARO:
        return {
          label: 'Em Preparo',
          className: 'bg-orange-100 text-orange-800 border-orange-200',
        };
      case PedidoStatus.PRONTO:
        return {
          label: 'Pronto',
          className: 'bg-purple-100 text-purple-800 border-purple-200',
        };
      case PedidoStatus.ENTREGUE:
        return {
          label: 'Entregue',
          className: 'bg-green-100 text-green-800 border-green-200',
        };
      case PedidoStatus.CANCELADO:
        return {
          label: 'Cancelado',
          className: 'bg-red-100 text-red-800 border-red-200',
        };
      default:
        return {
          label: 'Desconhecido',
          className: 'bg-gray-100 text-gray-800 border-gray-200',
        };
    }
  };

  const config = getStatusConfig();

  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium border ${config.className} ${className}`}
    >
      {config.label}
    </span>
  );
};

export default StatusBadge;