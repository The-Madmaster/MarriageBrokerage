import React from 'react';
import {
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  Box,
  Button,
} from '@mui/material';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { People, PersonAdd, Dashboard as DashboardIcon } from '@mui/icons-material';

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const getWelcomeMessage = () => {
    switch (user?.role) {
      case 'ADMIN':
        return 'Admin Dashboard - Manage brokers and oversee the system';
      case 'BROKER':
        return 'Broker Dashboard - Manage your clients and find matches';
      case 'CLIENT':
        return 'Client Dashboard - View your profile and potential matches';
      default:
        return 'Welcome to Mahi Marriage Brokerage';
    }
  };

  const getDashboardCards = () => {
    const commonCards = [
      {
        title: 'My Profile',
        description: 'View and edit your profile information',
        icon: <People />,
        action: () => navigate('/profile'),
      },
    ];

    switch (user?.role) {
      case 'ADMIN':
        return [
          ...commonCards,
          {
            title: 'Manage Brokers',
            description: 'View and manage all brokers in the system',
            icon: <People />,
            action: () => navigate('/brokers'),
          },
          {
            title: 'System Analytics',
            description: 'View system statistics and reports',
            icon: <DashboardIcon />,
            action: () => console.log('Analytics coming soon'),
          },
        ];
      case 'BROKER':
        return [
          ...commonCards,
          {
            title: 'My Clients',
            description: 'View and manage your assigned clients',
            icon: <People />,
            action: () => navigate('/clients'),
          },
          {
            title: 'Add New Client',
            description: 'Register a new client to your portfolio',
            icon: <PersonAdd />,
            action: () => navigate('/clients/add'),
          },
        ];
      case 'CLIENT':
        return [
          ...commonCards,
          {
            title: 'View Matches',
            description: 'Browse potential matches',
            icon: <People />,
            action: () => console.log('Matches coming soon'),
          },
        ];
      default:
        return commonCards;
    }
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        Welcome, {user?.fullName}!
      </Typography>
      <Typography variant="h6" color="text.secondary" gutterBottom>
        {getWelcomeMessage()}
      </Typography>

      <Grid container spacing={3} sx={{ mt: 2 }}>
        {getDashboardCards().map((card, index) => (
          <Grid size={{ xs: 12, sm: 6, md: 4 }} key={index}>
            <Card sx={{ height: '100%', cursor: 'pointer' }} onClick={card.action}>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  {card.icon}
                  <Typography variant="h6" sx={{ ml: 1 }}>
                    {card.title}
                  </Typography>
                </Box>
                <Typography variant="body2" color="text.secondary">
                  {card.description}
                </Typography>
                <Button variant="outlined" sx={{ mt: 2 }} fullWidth>
                  Access
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default Dashboard;