//COSTANTI
const PHOTOS_API_URL = "http://localhost:8080/api/v1/photos";
const contentDOM = document.getElementById("content");
const alertMessage = document.getElementById("alert");
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

const submitForm = async (event) => {
    event.preventDefault(); //per evitare comportamento predefinito di invio del form

    const messageInput = document.getElementById("message");
    const emailInput = document.getElementById("email");

    const message = messageInput.value;
    const email = emailInput.value;

    const messageData = {
        message,
        email,
    };

    try {
       const response = await axios.post(
       "http://localhost:8080/api/v1/messages",
       messageData
      );
      console.log(response.data);
      alertMessage.innerHTML = `<div class="alert alert-success col-6" role="alert">
                Message sent successfully
              </div>`;
    } catch (error) {
      console.log(error);
      alertMessage.innerHTML = `<div class="alert alert-danger col-6" role="alert">
                      Failed to send message. Please try again
                    </div>`;
    }

    //resetto campi dopo l' invio dei dati
    messageInput.value = "";
    emailInput.value = "";
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
    return `<div class="mt-4 alert alert-info"> Sorry but the list of photos appears to be empty :( </div>`;
  }
};

const loadData = async () => {
  //prende dati dall' api
  const data = await getPhotos();

  //appendo content a DOM
  contentDOM.innerHTML = createPhotoList(data);
};

// Funzione per filtrare le immagini in base al titolo
const filterPhotos = (data, searchTerm) => {
  return data.filter((element) => {
    return element.title.toLowerCase().includes(searchTerm.toLowerCase());
  });
};

// Funzione per gestire l'evento di ricerca
const handleSearch = async () => {
  const searchTerm = document.getElementById("keyword").value;
  const data = await getPhotos();

  // Filtra le immagini in base al titolo inserito dall'utente
  const filteredData = filterPhotos(data, searchTerm);

  // Aggiorna la visualizzazione con le immagini filtrate
  contentDOM.innerHTML = createPhotoList(filteredData);
};

// Aggiungi il gestore di eventi all'input di ricerca
document.getElementById("keyword").addEventListener("input", handleSearch);

//CODICE GLOBALE
loadData();
