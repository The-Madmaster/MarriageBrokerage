import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import mfaService from '../services/mfaService';
import { authService } from '../services/authService';

const MfaSetup: React.FC = () => {
    const [qrCodeUri, setQrCodeUri] = useState<string>('');
    const [manualSetupKey, setManualSetupKey] = useState<string>('');
    const [code, setCode] = useState<string>('');
    const [username, setUsername] = useState<string>('');
    const [error, setError] = useState<string>('');
    const navigate = useNavigate();

    useEffect(() => {
        const user = authService.getCurrentUser();
        if (user) {
            setUsername(user.username);
        }
        mfaService.setupMfa().then(
            response => {
                setQrCodeUri(response.data.qrCodeUri);
                setManualSetupKey(response.data.manualSetupKey);
            },
            error => {
                setError(error.response.data.message);
            }
        );
    }, []);

    const handleActivateMfa = (e: React.FormEvent) => {
        e.preventDefault();
        mfaService.activateMfa(username, code).then(
            () => {
                navigate('/dashboard');
                window.location.reload();
            },
            error => {
                setError(error.response.data.message);
            }
        );
    };

    return (
        <div className="container">
            <h2>Set up Multi-Factor Authentication</h2>
            <p>Scan the QR code with your authenticator app or manually enter the setup key.</p>
            {qrCodeUri && <img src={qrCodeUri} alt="QR Code" />}
            {manualSetupKey && <p>Manual Setup Key: {manualSetupKey}</p>}
            <form onSubmit={handleActivateMfa}>
                <div className="form-group">
                    <label htmlFor="code">Verification Code</label>
                    <input
                        type="text"
                        className="form-control"
                        id="code"
                        value={code}
                        onChange={(e) => setCode(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary">Activate MFA</button>
                {error && <div className="alert alert-danger mt-2">{error}</div>}
            </form>
        </div>
    );
};

export default MfaSetup;
