import React, { useEffect, useState } from 'react';
import { fetchTopGainers, warmUpBackend } from '../api/stockApi';

const Dashboard = () => {
  const [gainers, setGainers] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [initializing, setInitializing] = useState(true);

  useEffect(() => {
    const init = async () => {
      try {
        console.log("🔥 Warming up backend...");
        await warmUpBackend(); // Just triggers data prep (no response needed)
        setInitializing(false); // Done warming up
      } catch (err) {
        console.error("🥶 Backend warm-up failed:", err);
        setInitializing(false); // Still proceed to data fetch
      }
    };

    init();
  }, []);

  useEffect(() => {
    if (!initializing) {
      const loadGainers = async () => {
        try {
          console.log("📥 Fetching top gainers...");
          const data = await fetchTopGainers();
          setGainers(data);
        } catch (err) {
          console.error("🚨 Fetch failed:", err);
        } finally {
          setLoading(false);
        }
      };

      loadGainers();
    }
  }, [initializing]);

  return (
    <div>
      <h1>Top Gainers</h1>
      {loading || initializing ? (
        <p>{initializing ? 'Getting ready...' : 'Loading data...'}</p>
      ) : (
        <ul>
          {gainers.map((stock, index) => (
            <li key={index}>
              {stock.symbol} - {stock.companyName} - ${stock.c}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default Dashboard;
