import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { UserType } from '../../types';
import { LogOut, User, ChefHat, Users, Coffee } from 'lucide-react';

const Navbar: React.FC = () => {
  const { userName, userType, logout } = useAuth();

  const getUserIcon = () => {
    switch (userType) {
      case UserType.ADMIN:
        return <Users className="w-5 h-5" />;
      case UserType.COZINHA:
        return <ChefHat className="w-5 h-5" />;
      case UserType.GARCOM:
        return <Coffee className="w-5 h-5" />;
      default:
        return <User className="w-5 h-5" />;
    }
  };

  const getUserTypeLabel = () => {
    switch (userType) {
      case UserType.ADMIN:
        return 'Administrador';
      case UserType.CLIENTE:
        return 'Cliente';
      case UserType.COZINHA:
        return 'Cozinha';
      case UserType.GARCOM:
        return 'Garçom';
      default:
        return '';
    }
  };

  const getThemeColor = () => {
    switch (userType) {
      case UserType.ADMIN:
        return 'bg-gradient-to-r from-purple-600 to-purple-700';
      case UserType.COZINHA:
        return 'bg-gradient-to-r from-orange-600 to-red-600';
      case UserType.GARCOM:
        return 'bg-gradient-to-r from-blue-600 to-blue-700';
      default:
        return 'bg-gradient-to-r from-green-600 to-green-700';
    }
  };

  return (
    <nav className={`${getThemeColor()} shadow-lg`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-4">
            <ChefHat className="w-8 h-8 text-white" />
            <h1 className="text-xl font-bold text-white">Sistema Cardápio</h1>
          </div>
          
          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-2 text-white">
              {getUserIcon()}
              <div className="flex flex-col">
                <span className="text-sm font-medium">{userName}</span>
                <span className="text-xs opacity-80">{getUserTypeLabel()}</span>
              </div>
            </div>
            
            <button
              onClick={logout}
              className="flex items-center space-x-2 px-3 py-2 rounded-md text-white hover:bg-white/20 transition-colors duration-200"
            >
              <LogOut className="w-4 h-4" />
              <span className="text-sm">Sair</span>
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;