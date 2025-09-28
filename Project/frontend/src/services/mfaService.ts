import api from './api';

const setupMfa = () => {
    return api.post('/mfa/setup');
};

const activateMfa = (username: string, code: string) => {
    return api.post('/mfa/activate', { username, code });
};

const verifyMfa = (username: string, code: string) => {
    return api.post('/auth/verify', { username, code });
};

const mfaService = {
    setupMfa,
    activateMfa,
    verifyMfa,
};

export default mfaService;
