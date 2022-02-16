const ShowData = ({ results, dataArrayMeta, propsCheck, tableName }) => {
  propsCheck();
  return (
    <div>
      {/* <p>Number of Objects : {Object.keys(dataArrayMeta[tableName]).length} </p> */}
      <table>
        <thead>
          <tr>
            {Object.entries(dataArrayMeta[tableName]).filter(([k, v]) => v !== tableName).map(([k, v]) =>
              <th key={k}>{JSON.stringify(v)}</th>
            )}
          </tr>
        </thead>
        <tbody>
          {results.map(ele =>
            <tr key={ele[Object.keys(results[0])[0]]}>
              {Object.entries(ele).map(([key, value]) =>
                <td key={key}>{JSON.stringify(value)}</td>
              )}
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default ShowData;
