import React, {useState} from 'react';
import axios from 'axios'
import Card from 'reactstrap'
const ProductList = () => {
  const [productList, setProductLists] = useState([]);
  axios.get("http://localhost:8080/api/v1/products/lists").then(
    res => { console.log(res.data)
  setProductLists(res.data)
    }
  )

  // var config = {
  //     method: 'get',
  //     maxBodyLength: Infinity,
  //     url: 'http://localhost:8080/api/v1/products/lists',
  //     headers: {
  //         'Authorization': 'Basic YWRqYTpwYXNzZXI='
  //     }
  // };

  // axios(config)
  //     .then(function (response) {
  //         console.log(JSON.stringify(response.data));
  //     })
  //     .catch(function (error) {
  //         console.log(error);
  //     });
  return (
    <div>

      {/* var axios = require('axios');
var data = JSON.stringify({
  "product_price": 12000,
  "product_label": "cement02",
  "product_type": "Cementt",
  "product_description": "this is my first product"
});

var config = {
  method: 'get',
maxBodyLength: Infinity,
  url: 'localhost:8080/api/v1/products/lists',
  headers: { 
    'Authorization': 'Basic YWRqYTpwYXNzZXI=', 
    'Content-Type': 'application/json'
  },
  data : data
};

axios(config)
.then(function (response) {
  console.log(JSON.stringify(response.data));
})
.catch(function (error) {
  console.log(error);
});
 */}
      <h2>Catalog</h2>
      <div>
        {
          productList.map((product) => {
            <div key={product.product}>

            </div>
          })
        }
      </div>
    </div>
  );
};

export default ProductList;