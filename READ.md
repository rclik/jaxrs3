# Deployment Descriptor 3 Ile JAX-RS Uygulamasi Yazilmasi

Java Servlet 3.0 api ve sonrasinda Deployment Descriptor 3. versiyonu ile web.xml e gerek kalmadan JAX-RS uygulamasi
yazilabilir. Bu versiyonda annotation lar ve ve bazi ozel class lar kullanilarak web.xml de yapilan tanimlamalari 
programatik olarak tanimlayabiliriz. 

## JAX-RS Uygulamasinin Tanitilmasi
Uygulamanin icindeki ozel class lari tanimlamak icin bir application class i yazilir. Bu class in basina 
**javax.ws.rs.ApplicationPath** annotation eklenir. Bu sekide Java servlet container ayaga kalkarken bu class in 
varligindan haberdar olur. Ve onu ayaga kaldirir.

Ayrica web.xml de tanimladigimiz servlet vb. servlet api elemanlarini tanimlayabilmemiz icin class 
**import javax.ws.rs.core.Application** class ini extend etmelidir. Bu class in bazi method larini override ederek
servlet api elemanlarini tanimlayabiliriz.

Application class inin servlet container (RestEasy implementation) tarafindan aranmasi icin ise RestEasy nin bir 
dependency (servlet-initializer) sinin eklenmesi lazim. Yoksa servlet container initialize olurken annotation lari aramadigindan application
class imizi ayaga kaldirmaz:

    ...
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-servlet-initializer</artifactId>
            <version>${resteasy.version}</version>
        </dependency>
    ...

Simdi ise ilk olarak application class imizi yazalim:

    @ApplicationPath("/")
    public class RestApplication extends Application {
    }

Bu sekilde, servlet container ayaga kalkarken uygulamamiz ayaga kaldirilir.

Uygulamamiza bir context verebiliriz. Bu sekilde uygulamamizi ayni servlet container uzerinde baska uygulamalardan
ayiran bir context e sahip oluruz. su sekilde calisir:
> http://localhost:8080/rootContext/applicationContext/resource ...

Yani ilk olarak servlet in context i geliyor, buna root context diyebiliriz. Olmak zorunda degildir. Sonrasinda ise 
application imizin context i gelir. 

Application context ini tanimlarken dikkat edilmesi gereken durumlar vardir:
* tum url (/*) lere izin verilmemelidir, cunku diger application larla cakisabilir. Ayrica url in alt uzantilari icin de
calisacaktir. O zaman application context inin altindaki tum url lere de izin vermemeliyiz.
* application context i altindaki tum url lere de izin verilmemeli. (/applicationContext/*) 
* ya hic verilmemeli, (/ veya hic) veya sadece application context i verilmeli. 

Genel olarak ne kadar az izin verilirse o kadar uygulamayi kontrolumuz altinda almis oluruz:

     // @ApplicationPath("/") OK
     // @ApplicationPath("/*") Don't use /*
     // @ApplicationPath("/root-path/*") Don't use /*
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

Cookie Islemleri

normalde yapildigi gibi http request i uzerinden de cookie islemleri yapilabilir ama, jaxrs sagladigi yontemleri de kullanabiliriz.
CookieParam ann. kullanarak cookie de alabliriz.

ilk olarak cookie ekleyelim. burada dikkat etmemiz gereken sey ise artik method larimiz String yerine javax.ws.rs.core.Response donmesi
gerekiyor. cunku cookie ler Response object i icinde tutulurlar.

Hatirlatmak da fayda var. Cookie leri domain e gore, path ine gore verebilirsin. cookie lerin version unu da verebilirsin.

@BeanParam

Soyle bir durum dusun. Bir java object ini URL uzerinden alman gerektigi durumlar olabilir. O zaman bir method da birden fazla annotation
ile bu data lari almak yerine BeanParam ann. ini kullanarak URL deki data yi kullanarak o object e atanmasini saglayabilirsin.

product/getProductById/computer?id=100 
mesela burada product urunumuz var. biz onu category si ve id bilgisiyle almak istiyoruz. method umuzu tasarlarken bu data lari farkli fakli 
map lemek yerine @BeanParam kullanarak istenilen map lemeyi yapabiliz. 
BeanParam ile yapilabilecek map lemeler:
    @CookieParam
    @FormParam
    @HeaderParam
    @MatrixParam
    @PathParam
    @QueryParam
Bunun avantaji ise resource method larinin okunmasini ve yazilmasini kolaylastirarak, URL uzerinden gelen data nin bean ile inject edilmesini
saglamaktir.

@DefaultValue Annotation i
@DefaultValue Annotation i @*Param olarak gecen diger tum JaxRs annotation larinin eger value lari yoksa default degerlerini atar.
oldukca basit bir uygulama.

@Encoded
    http query, path ve matrix parametrelerini HTTP spec lerine gore encode eder. @Encode parametresi ise encode edilmis
    parametre degerlerinin decode edilmesini engeller. bazen bu sekilde parametreler isimize yarayabilir.
    bu parametreyi class, method ve parametre seviyesinde kullanilabilir.
    Kullanimi ise basit, sadece parametrenin basina @Encoded
    
    
@Context Annotation

Context annotation i ile asagidaki object leri inject edebilirsin:
    javax.ws.rs.core.HttpHeaders
    javax.ws.rs.core.UriInfo
    javax.ws.rs.core.Request
    javax.servlet.http.HttpServletRequest
    javax.servlet.http.HttpServletResponse
    javax.servlet.ServletConfig
    javax.servlet.ServletContext
    javax.ws.rs.core.SecurityContext
Bunlar goruldugu gibi http request response servlet config leri context leri security context i gibi bilgileri alabilmek
icin kullanilirlar.

bunlari kullanabilmek icin project imizde servlet-api dependency lerinin eklenmesi gerekir:

    <properties>
    .....
    <servlet.version>3.1.0</servlet.version>
    </properties>
    ...
    <!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>${servlet.version}</version>
    </dependency>

bu sekilde servlet api eklenmis oldu.


Standard Entity Providers
    Entity Provider lari http request ve response larin body lerinin olusturulmasinda kullanilirlar.
    Request icin MessageBodyReader response icin MessageBodyWriter kullanilir.
    Provider lar sunlardir:
    
    byte[]                  — All media types (*/*)
    java.lang.String        — All text media types (text/*)
    java.io.InputStream     — All media types (*/*)
    java.io.Reader          — All media types (*/*)
    java.io.File            — All media types (*/*)
    javax.activation.DataSource — All media types (*/*)
    javax.xml.transform.Source — XML types (text/xml, application/xml and application/*+xml)
    javax.xml.bind.JAXBElement and application-supplied JAXB classes XML media types (text/xml, application/xml and application/*+xml)
    MultivaluedMap<String, String> — Form content (application/x-www-form-urlencoded)
    StreamingOutput — All media types (*/*), MessageBodyWriter only

herbir tip icin farkli media type header i kullanilir. bunlara dikkat etmekte fayda var.
response lar bir stream olarak verilebilirler, bu sekilde file da response a koyabiliriz.

Ilk olarak StreamOutput interface ini kullanalim, bu bir stream ve http response header olarak da text_plain eklememiz gerekir.
default olarak text/html;charset=UTF-8 olarak geliyor, biz ekleigimiz de ise text/plain;charset=UTF-8 olarak degistigini gormus olduk

Entity Providers olarak byte array kullanabiliriz. tum media type lari icin kullanilabilir.

vb isler icin kullanilir.


JAXB ve XML
    JAXB Java Architecture for XML binding olarak acilir.
    JAX-RS de JAXB kullanarak xml body ler donulebilir veya xml body ler alinabilir, server tarafindan.
    xml icerigi java object ine cevirme islemine unmarshalling, java object i xml e cevirme islemine marshalling denir.

bu islemleri yapmak icin rest easy ye bir dependency eklemek lazim:

        <dependency>
        	<groupId>org.jboss.resteasy</groupId>
        	<artifactId>resteasy-jaxb-provider</artifactId>
        	<version>${resteasy.version}</version>
        </dependency>
        
bunu eklemezsen soyle bir hata alirsin: Could not find MessageBodyWriter for response object of type

bu islemler bittikten sonra eger produces annotation i eklemzsek http response header Content-Type: application/xml;charset=UTF-8
sekilde eklenmis olur. Produces (MediaType.APPLICATION_XML) eklersen de aynisi olur application/xml;charset=UTF-8

XML e donusturecek object in bazi parametreleri:

    ...
    @XmlRootElement(name = "person")
    @XmlAccessorType(XmlAccessType.FIELD)
    public class Person {
    
        @XmlElement
    ...

XmlRootElement -> response da donecek xml in root tag name icin kullanilir.
XmlAccessType -> response da donecek xml in object in nesi kullanilarak generate edilecegini soylemek icin kullanilir.
XmlElement -> hangi field in donusturulecegini yazmak icin kullanilir.
XmlTransient -> ise hangi field in gosterilmeyecegini belirtmek icin kullanilir.

JSON and Jackson

java object inin json a cevirilmesi ve tam tersini yapabilmek icin jackson provider library sini kullanicaz.

rest easy icin jackson provider library dependency si:

    <!-- RESTEasy JAXB Provider -->
    <!-- https://mvnrepository.com/artifact/org.jboss.resteasy/resteasy-jaxb-provider -->
    <dependency>
    	<groupId>org.jboss.resteasy</groupId>
    	<artifactId>resteasy-jackson-provider</artifactId>
    	<version>${resteasy.version}</version>
    </dependency>

her rest easy version inin kendi jackson provider i oldugunu unutmamak lazim ayni jaxb de oldugu gibi

person doneceksen response header koyman lazim
@Produces(MediaType.APPLICATION_JSON)

jackson icin ozel bir annotation kullanmamiza gerek kalmiyor. direk olarak servlet container (rest easy) java object ini
json a cevirir. bunu yaparken jaxb resource un varsa onun calismasini da bozmaz.

cevirme islemi produces annotation i ile saglaniyor, onu cikarirsak servlet container object i json a cevirmiyor.



JSON and Jettison

jettison java objet - json marshalling ve unmarshalling yapar. ama burada java object i jaxb annotation larina sahip
olabilir. jettison kutuphanesi de onlari anlamlandirarak json object i olusturur.

yeni bir dependency eklemek lazim: 

    <dependency>
    	<groupId>org.jboss.resteasy</groupId>
    	<artifactId>resteasy-jettison-provider</artifactId>
    	<version>${resteasy.version}</version>
    </dependency>	
    
burada jackson library sini kaldirmak gerekir yoksa cakismalar olacaktir.

Server Response code 


javax.ws.rs.core.Response

Response icin kullanilir, abstract class dir. ResponseBuilder class i uzerinden olusturulabilirler.

    	@Path("/name")
    	public Response getPersonNameById() {
    		String personName = "Levent";
    		ResponseBuilder builder = Response.ok(personName);
    		// add header
    		builder.header("myHeader", "myHeaderValue");
    		// add cookie
    		builder.cookie(new NewCookie("myCookie", "myCookieValue"));
    		return builder.build();
    	}
    	
here we create a response has:
    200 OK 
    new header
    cookie
by using

WebApplicationException

web uygulamamiz kullaniciya uygun response donebilir ya da exception ile duruma uygun bilgilendirme yapilabilir.

Eger atilan exception lar icin exception mapper tanimlandiysa, uygulamadan atilan exception lar mapper tarafindan
yakalanirlar. Uygulamaya gore kullaniciya donus yapilir.

WebApplicationException ve turev class lari ise exception mapper class ina gerek duymadan servlet container tarafindan
yakalanirlar. Yakalandiginda ise getResponse method u kullaniciya status code u ile gonderilirler.

Exception mapper i olmayan class lar ise container tarafindan handle edilirler ve direk olarak 500 response code ve
exception icerigiyle kullaniciya donulurler.

WebApplicationException 

Cok basit olarak bir ornek yapalim;
bir class service imiz olsun, eger istenilen id de customer bulamazsa 404 not found donsun
bir method da not implemented (501) donsun. yani service daha yazilmamis anlamina gelen response code
WebApplicationException RuntimeException dan turemistir.

    	@GET
    	@Path("/customer/{id}")
    	public Customer getCustomer(@PathParam("id") int id) {
            System.out.println("getCustomer is called....");
            CustomerService service = new CustomerService();
    
    		Customer customer = service.findCustomer(id);
    		if (customer == null) {
    			throw new WebApplicationException(Response.Status.NOT_FOUND);
    		}
    		return customer;
    	}
    
    	@GET
    	@Path("/name/{id}")
    	public String getCustomerName(@PathParam("id") int id) {
            System.out.println("getCustomerName is called...");
    		throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
    	}

Exception Mappers
web application imiz exception atacak sekilde olusturabiliriz ve bu exception lari da exception mapper class inda 
yakalayip orada ilgili alanlari kullanarak javax.ws.rs.Response class i olusturabiliriz.

uygun bir uygulama olarak RunTimeException dan kendi exception larimizi turetebiliriz.
bu sekilde method signature larina exception lari eklememize gerek kalmaz. ne de olsa exception mapper da yakalayacagiz. 

ornek olarak CustomerNotFoundException olusturalim, RunTimeException dan turetelim

son olarak bu exception mapper class ini da servlet container a bildirmemiz lazim. Onun icin de RestApplication sinifina
bu class i diger resource lar gibi eklememiz gerekir.

yeni exception class i ve mapper yazmak yerine jax-rs in bizim icin gelistirdigi exception lari kullanabiliriz.















