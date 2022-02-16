import { Fragment, useState } from "react";

const TableDataController = ({ tableName, dataArrayMeta, setReqTpe }) => {
  const [query, setQuery] = useState(tableName);
  const [newQuery, setnewQuery] = useState([]);
  const tableData = Object.entries(dataArrayMeta[tableName]);
  const nbr = Object.entries(dataArrayMeta[tableName]).length;
  console.log(newQuery);
  console.log(query);
  const skadoosh = () => setReqTpe(query);
  const chemBody = () => {
    const t = []
    for (let index = 0; index < nbr; index++) {
      if (newQuery[index] !== undefined) {
        if (newQuery[index][0] !== undefined) {
          if (newQuery[index][0].length === 0) {
            t.splice(index, 1, "");
          } else {
            t.push(`AND ${tableData[index + 1][1]} ='${newQuery[index][0]}' `);
          }
        }
      }
    }
    console.log(t)
    t.unshift(" WHERE");
    t.unshift(tableName);
    var finalQuery = t.join('').replace('AND', '').trim() //Replace 1st Occurence of 'AND'
    if (finalQuery[finalQuery.length - 1] === 'E' && finalQuery[finalQuery.length - 2] === 'R') finalQuery = finalQuery.slice(0,finalQuery.indexOf('WHERE'));
    setQuery(finalQuery.trim());
  }
  const changeQuery = e => {
    const myQuery = e.target.value;
    const queryId = e.target.id - 1;
    console.log(`Query : ${myQuery} id: ${queryId}`)
    if (newQuery[queryId] !== undefined && newQuery[queryId][0] !== undefined) {
      newQuery.splice(queryId, 1, [myQuery]);
    } else {
      //newQuery.push([myQuery]);
      newQuery[queryId] = [myQuery];
    }
    chemBody();
  }
  return (
    <div>
      {tableName} : Controller
      <form onSubmit={(e) => e.preventDefault()}>
        {Object.entries(dataArrayMeta[tableName]).filter(([k, v]) => v !== tableName).map(([k, v]) =>
          <Fragment key={k}>
            <label>{`${v} : `}</label>
            <input id={k} autoFocus type="text" size="10" onInput={changeQuery} />
            <br />
            <br />
          </Fragment>
        )}
        <button type="submit" onClick={skadoosh}>Skadoosh</button>
      </form>
      <p>{query}</p>
    </div>
  );
}

export default TableDataController;
