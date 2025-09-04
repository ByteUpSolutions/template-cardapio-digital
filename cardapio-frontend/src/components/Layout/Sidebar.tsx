import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { UserType } from '../../types';
import { 
  Home, 
  Menu, 
  ShoppingCart, 
  Users, 
  ChefHat, 
  Coffee,
  BarChart3,
  Settings
} from 'lucide-react';

interface SidebarProps {
  activeTab: string;
  onTabChange: (tab: string) => void;
}

const Sidebar: React.FC<SidebarProps> = ({ activeTab, onTabChange }) => {
  const { userType } = useAuth();

  const getNavItems = () => {
    switch (userType) {
      case UserType.ADMIN:
        return [
          { id: 'dashboard', label: 'Dashboard', icon: Home },
          { id: 'cardapio', label: 'Cardápio', icon: Menu },
          { id: 'pedidos', label: 'Pedidos', icon: ShoppingCart },
          { id: 'usuarios', label: 'Usuários', icon: Users },
          { id: 'relatorios', label: 'Relatórios', icon: BarChart3 },
          { id: 'configuracoes', label: 'Configurações', icon: Settings },
        ];
        
      case UserType.CLIENTE:
        return [
          { id: 'cardapio', label: 'Cardápio', icon: Menu },
          { id: 'carrinho', label: 'Carrinho', icon: ShoppingCart },
          { id: 'pedidos', label: 'Meus Pedidos', icon: ShoppingCart },
        ];
        
      case UserType.COZINHA:
        return [
          { id: 'dashboard', label: 'Dashboard', icon: Home },
          { id: 'pedidos', label: 'Pedidos', icon: ChefHat },
          { id: 'cardapio', label: 'Cardápio', icon: Menu },
        ];
        
      case UserType.GARCOM:
        return [
          { id: 'dashboard', label: 'Dashboard', icon: Home },
          { id: 'pedidos', label: 'Pedidos', icon: Coffee },
          { id: 'mesas', label: 'Mesas', icon: Users },
          { id: 'cardapio', label: 'Cardápio', icon: Menu },
        ];
        
      default:
        return [];
    }
  };

  const navItems = getNavItems();

  const getThemeColors = () => {
    switch (userType) {
      case UserType.ADMIN:
        return {
          bg: 'bg-purple-50',
          activeItem: 'bg-purple-600 text-white',
          inactiveItem: 'text-purple-700 hover:bg-purple-100',
        };
      case UserType.COZINHA:
        return {
          bg: 'bg-orange-50',
          activeItem: 'bg-orange-600 text-white',
          inactiveItem: 'text-orange-700 hover:bg-orange-100',
        };
      case UserType.GARCOM:
        return {
          bg: 'bg-blue-50',
          activeItem: 'bg-blue-600 text-white',
          inactiveItem: 'text-blue-700 hover:bg-blue-100',
        };
      default:
        return {
          bg: 'bg-green-50',
          activeItem: 'bg-green-600 text-white',
          inactiveItem: 'text-green-700 hover:bg-green-100',
        };
    }
  };

  const colors = getThemeColors();

  return (
    <div className={`w-64 ${colors.bg} shadow-lg h-full`}>
      <div className="p-6">
        <nav className="space-y-2">
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = activeTab === item.id;
            
            return (
              <button
                key={item.id}
                onClick={() => onTabChange(item.id)}
                className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-all duration-200 ${
                  isActive ? colors.activeItem : colors.inactiveItem
                }`}
              >
                <Icon className="w-5 h-5" />
                <span className="font-medium">{item.label}</span>
              </button>
            );
          })}
        </nav>
      </div>
    </div>
  );
};

export default Sidebar;