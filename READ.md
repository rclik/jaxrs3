Simdi bir de web deployment descriptor 3 ile yapalim. Yani Java Servlet 3.0 api ve sonrasinda bu var. Bir application i belirtmek icin Application class inin basina
@ApplicationPath annotation ini kullanicaz.
Sadece application path yetmez ama, bir de annotation lari arama yapmasi icin bir kod girmemiz gerekiyor. onu da ayni servlet api yi rest easy nin implement ettigi gibi servlet 
initialization ini da implement ettigi bir library uzerinden yapicagiz.

simdi ise koyulalim;

ilk olarak root resource umuzu yazalim, bu ayni degisen birsey yok.

sonrasinda application i tanitacagimiz class i da yazalim. ama bu kere uzerine @ApplicationPath annotation ini kullanalim:

    //@ApplicationPath("/") OK
    //@ApplicationPath("/*") Don't use /*
    //@ApplicationPath("/root-path/*") Don't use /*
    @ApplicationPath("/root-path")
    public class RestApplication extends Application {
    
        Set<Object> singletons = new HashSet<>();
    
        public RestApplication() {
            singletons.add(new RestMessageController());
        }
    
        @Override
        public Set<Object> getSingletons() {
            return singletons;
        }
    }
 
 root context i verirken dikkat etmemiz gereken seyler var:
    tum url lere bakmamasi lazim, onun icin /* i kullanmamak lazim ne kadar erken birakirsak o kadar iyi, yoksa bir diger resource lar icin de aramaya devam edicek
    ayni sekilde bir path ve sonrasindaki tum sub path leri de aramasina izin vermemeliyiz. ayni mantikla; /path/* kullanma
    
@Path annotation i 
HTTP istegi ile resource lari birbirleriyle baglanmasini saglayan annotation idir.

ayni resoruce uzerinde sadece sub resource lar da olusturabiliriz. yani path class icin bir olacak ve class method lari icin de path annotation i kullanilacak.

Sub-resouce yapmak icin soyle bir yontem kullanabilirsin. soyle dusun; a vd b resource lari c nin sub resource u olsun. o zaman main resource a bir path verirsin.
sonra da main resource icinde sub-resource lari dondurecek birer method olur. ve bu method lar uzerinde ise sub-resouce path leri olur:

    @Path("/c")
    class C {
        @Path("/a")
        public A getA(){
            return new A();
        }
        @Path("/b")
        public B getB(){
            return new B();
        }
    }

sonra sub-resource lar uzerinde http method lari kullanabilrsin GET, PUT, DELETE gibi

ornek larak shoppingStore-product ornegine bakabilirsin:

Path Parametresi okumak

Path parametresi url e yazilan parametredir:
    /customer/100
bu 100 id li customer i get etmek icin kullanilan rest url i olabilir.

bunun icin, resource method unda @PathParam("id") String customerId olarak method a alabiliriz. bu id yi alabilmek icin Path ann. de kullanmamiz gerekiyor.
path ann. icinde url in neresinden alacagimizi ve aldigimiz parametresinin ismini belirtmemiz gerekiyor. Bu isimi kullanarak pathParam ann. icinde kullanicaz.
o sekilde java object e map lama yapmis olucagiz.

    @Path("/customer")
    public class CustomerResource {
        @GET
        @Path("/{id}")
        public String getCustomerWithId(@PathParam("id") String customerId) {
            return String.format("Customer with id %s is requested ", customerId);
        }
    }
    
birden fazla parametre alabilmek icin:

    @GET
    @Path("/{first-name}-{last-name}")
    public String getCustomerWithFirstNameAndLastName(@PathParam("first-name") String firstName, @PathParam("last-name") String lastName) {
        return String.format("Customer with first name %s and last name %s is requested", firstName, lastName);
    }
    
PathParam in int olmasini istiyorsak, regex ile de isimizi halledebiliriz:

    @GET
    @Path("/int/{id : \\d+}")
    public String getCustomerWithIntId(@PathParam("id") int id) {
        return String.format("Customer with id %s is requested", id);
    }
    
bu sekilde yaparsak regex e uyan rest request i cagirilacak yoksa 404 atilir.

bir ornek de string ve int regex li olan olsun:

    @GET
    @Path("/identifier/{identifier : ID-\\d+\\w+}")
    public String getCustomerWithIdentifier(@PathParam("identifier") String identifier){
        return String.format("Customer with identifier %s is requested", identifier);
    }

/customer/identifier/ID-sdf27jhsdf calismazken
/customer/identifier/ID-27jhsdf calisir
ID id olsa calismaz.

PathSegment ve @MatrixParam

.../path/matrixParam=value; matrixParam2=value2 ; matrixParam3=value3

bu sekilde verilen URI parametrelerini alabilmek icin @PathSegment i kullaniyoruz.
birden fazla parametre okuyabilmek ve bu parametrelerin name lerine gore java obje lerine map lememizi saglayan path param seklidir.

http://localhost:8080/app/root-path/car/criteria;brand=mercedes;color=red;color=blue
uri bu sekilde;

    @GET
    @Path("/{search : criteria}")
    public String getCarWithSearchCriteria(@PathParam("search") PathSegment pathSegment) {
        String brand = pathSegment.getMatrixParameters().getFirst("brand");
        List<String> colors = pathSegment.getMatrixParameters().get("color");

        return String.format("Car with brand: %s and color: %s is called", brand, colors);
    }
    
bu tip parametreleri coklu parametre okurken kullanabiliriz.
 
bir de aradaki tum path parametrelerini alabilmek icin PathSegment i kullanabiliriz.
    
    @GET
    @Path("/{model : .+}/year/{year}")
    public String getCarByYear(@PathParam("model") List<PathSegment> car, @PathParam("year") String year) {
        return String.format("Car with properties %s and year %s is requested", car, year);
    }
/app/root-path/car/mercedes/red/blue/year/2016
buradaki mercedes, red ve blue kismini List<PathSegment> objesi ile aliyoruz.

PathSegment yerine ayni isi yapan Matrix param i da kullanabiliriz;


car;brand=Mercedes;color=red;color=blue;
    
    @GET
    public String getCarWithProperties(@MatrixParam("brand") String brand, @MatrixParam("color") List<String> colors){
        return String.format("Car with brand: %s and color: %s is called", brand, colors);
    }
    
ilk ornegin aynisini yapabilmek icin kullanabiliriz.

Query Param:
/path?queryParam=value&queryParam2=value2&queryParam3=value3
bu sekilde gelen request leri handle etmek icin, 

    @GET
    public String getBookWithId(@QueryParam("id") int id){
        return String.format("Book with id %s is requested", id);
    }
    
bu kadar, path param kadar yetenekli degil gibi, mesela regex veremiyorsun.

UriInfo uzerinden de query param larini alabiliriz. onu da @Context ann. kullanarak aliyoruz.

Bunlari kullanirken problem olan sey ise method signature lari ayni olunca servlet nasil onu map liyecegini bilemeyebilir.
onun icin http method larinda degisiklik yapman gerekebilir:

    @GET
    public String getBookWithId(@QueryParam("id") int id) {
        return String.format("Book with id %s is requested", id);
    }

    @GET
    public String getBookByName(@Context UriInfo info) {
        String name = info.getQueryParameters().getFirst("name");
        return String.format("Book with name %s is requested. ", name);
    }
Burada oldugu gibi, burada sadece ilk olan calisiyor, ikinsi ise calismiyor.

FormParam ve Form Annotation lari.

Aslinda bu html in form icin kullandigi yapiyi rest te de kullanmak icin gelistirilmis yontem.
Farkli olarak 

    The @javax.ws.rs.FormParam annotation is used to access 
    application/x-www-form- urlencoded request bodies.
    
yani url encoded request body lere ulasmak icin kullnilir, bu da daha secure bir iletisim saglar.

@FormParam("name") String name 

olarak kullaniliyor baska bir farklilik yok ama form larda kullanilan http method unu kullanmamiz: POST


@HeaderParam

Burada gelen request lerin header larini aliyorsunuz.
Bunu @Context den de alabiliriz. @Context HttpHeaders headers
Ya da @HeaderParam("header-name") i kullanarak da alabilirsin.



