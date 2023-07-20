//COSTANTI
const PHOTOS_API_URL = "http://localhost:8080/api/v1/photos";
const contentDOM = document.getElementById("content");
console.log(contentDOM);

//API
//sto dichiarando una funzione asincrona (perchè la get di axios è asincrona)
//await = "aspetta che axios.get mi ritorni qualcosa"
const getPhotos = async () => {
  try {
    const response = await axios.get(PHOTOS_API_URL);
    console.log(response);
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

//DOM MANIPULATION
//funzione che prende in ingresso i dati e ne fa una lista
const createPhotoList = (data) => {
  if (data.length > 0) {
    let list = `<div class="row my-3">`;

    data.forEach((element) => {
      if (element.visible) {
        list += `
        <div class="col g-3 d-flex justify-content-center hover-icons">
            <img style="width:300px;" alt="${element.description}" src="http://localhost:8080/files/image/${element.id}">
        </div>`;
      }
    });

    list += `</div>`;

    return list;
  } else {
    return `<div class="alert alert-info"> Sorry but the list of photos appears to be empty :( </div>`;
  }
};

const loadData = async () => {
  //prende dati dall' api
  const data = await getPhotos();

  //appendo content a DOM
  contentDOM.innerHTML = createPhotoList(data);
};

//CODICE GLOBALE
loadData();
