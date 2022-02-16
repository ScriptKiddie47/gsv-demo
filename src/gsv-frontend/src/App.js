import './App.css';
import Header from './Components/Header';
import ShowData from './Components/ShowData';
import TableDataController from './Components/TableDataController';
import TableMeta from './Components/TableMeta';

import { useEffect, useState } from "react";


function App() {
  const dataArrayMeta = {
    "Historicalinvesttemp_TBL": {
      "0": "Historicalinvesttemp_TBL",
      "1": "Year",
      "2": "Stocks",
      "3": "T_Bills",
      "4": "T_Bonds"
    }
  }

  const [results, setResults] = useState([]);
  const [reqType, setReqTpe] = useState('Historicalinvesttemp_TBL');
  const propsCheck = () => console.log("Hello");
  const tableName = "Historicalinvesttemp_TBL";
  const API_URL = 'http://localhost:8080/showMeData/';
  useEffect(() => {

    const fetchItems = async () => {
      try {
        const response = await fetch(`${API_URL}${reqType}`);
        if (!response.ok) throw new Error("Did not receve expected data");
        const results = await response.json();
        setResults(results)
      } catch (err) {
        console.log(err);
      }
    }

    fetchItems();
  }, [reqType]);
  return (
    <div>
      <Header />
      <TableMeta />
      <TableDataController tableName={tableName} dataArrayMeta={dataArrayMeta} setReqTpe={setReqTpe}/>
      <ShowData results={results} dataArrayMeta={dataArrayMeta} propsCheck={propsCheck} tableName={tableName} />
    </div>
  );
}

export default App;
