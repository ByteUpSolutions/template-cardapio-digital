import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { UserType } from '../types';
import Navbar from '../components/Layout/Navbar';
import Sidebar from '../components/Layout/Sidebar';
import ProtectedRoute from '../components/ProtectedRoute';

// Import components for different user types
import Dashboard from './Dashboard';
import CardapioManagement from '../components/Admin/CardapioManagement';
import CardapioView from '../components/Cliente/CardapioView';
import CartView from '../components/Cliente/CartView';
import PedidosCozinha from '../components/Cozinha/PedidosCozinha';

const MainLayout: React.FC = () => {
  const { userType } = useAuth();
  const [activeTab, setActiveTab] = useState('dashboard');

  const renderContent = () => {
    switch (userType) {
      case UserType.ADMIN:
        switch (activeTab) {
          case 'dashboard':
            return <Dashboard />;
          case 'cardapio':
            return <CardapioManagement />;
          case 'pedidos':
            return <div className="text-center py-8 text-gray-500">Gestão de Pedidos (Em desenvolvimento)</div>;
          case 'usuarios':
            return <div className="text-center py-8 text-gray-500">Gestão de Usuários (Em desenvolvimento)</div>;
          case 'relatorios':
            return <div className="text-center py-8 text-gray-500">Relatórios (Em desenvolvimento)</div>;
          case 'configuracoes':
            return <div className="text-center py-8 text-gray-500">Configurações (Em desenvolvimento)</div>;
          default:
            return <Dashboard />;
        }
        
      case UserType.CLIENTE:
        switch (activeTab) {
          case 'cardapio':
            return <CardapioView />;
          case 'carrinho':
            return <CartView />;
          case 'pedidos':
            return <div className="text-center py-8 text-gray-500">Meus Pedidos (Em desenvolvimento)</div>;
          default:
            return <CardapioView />;
        }
        
      case UserType.COZINHA:
        switch (activeTab) {
          case 'dashboard':
            return <Dashboard />;
          case 'pedidos':
            return <PedidosCozinha />;
          case 'cardapio':
            return <CardapioView />;
          default:
            return <Dashboard />;
        }
        
      case UserType.GARCOM:
        switch (activeTab) {
          case 'dashboard':
            return <Dashboard />;
          case 'pedidos':
            return <div className="text-center py-8 text-gray-500">Gestão de Pedidos do Garçom (Em desenvolvimento)</div>;
          case 'mesas':
            return <div className="text-center py-8 text-gray-500">Gestão de Mesas (Em desenvolvimento)</div>;
          case 'cardapio':
            return <CardapioView />;
          default:
            return <Dashboard />;
        }
        
      default:
        return <div className="text-center py-8 text-gray-500">Tipo de usuário não reconhecido</div>;
    }
  };

  // Define o primeiro tab baseado no tipo de usuário
  React.useEffect(() => {
    switch (userType) {
      case UserType.CLIENTE:
        setActiveTab('cardapio');
        break;
      default:
        setActiveTab('dashboard');
    }
  }, [userType]);

  return (
    <ProtectedRoute>
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <div className="flex">
          <Sidebar activeTab={activeTab} onTabChange={setActiveTab} />
          <main className="flex-1 p-8">
            {renderContent()}
          </main>
        </div>
      </div>
    </ProtectedRoute>
  );
};

export default MainLayout;