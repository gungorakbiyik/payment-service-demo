# payment-service-demo

Bu proje, Spring Boot uygulamalarında `@EventListener + @Async` kombinasyonunun yanlış kullanımından kaynaklanan bir **race condition bug'ının simülasyonudur**.

## Bug Nedir?

`PaymentService`, bir ödeme işlemini tamamladıktan sonra `@Async @EventListener` ile dinlenen bir event fırlatıyor. Ancak event, `@Transactional` metodun **commit edilmesinden önce** yayınlanıyor.

Bu durumda async listener, henüz commit edilmemiş entity'yi eski haliyle (stale read) okuyor ve kendi güncellemesini yazıyor. Ardından `PaymentService` commit ettiğinde, listener'ın değişikliklerinin üzerine yazıyor — **veri kaybı**.

Hata aralıklı görünür çünkü `AuditService`'deki gecikme değişken; thread scheduling'e bağlı olarak kimi zaman bug tetiklenir, kimi zaman tetiklenmez.

## Çözüm

`@EventListener` yerine `@TransactionalEventListener` kullanmak:

```java
@Async
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void onPaymentCompleted(PaymentCompletedEvent event) { ... }
```

`AFTER_COMMIT` sayesinde event, publisher'ın transaction'ı commit ettikten sonra tetiklenir. Listener yeni bir transaction açarak temiz bir state üzerinde çalışır.

## Detaylı Yazı

Konunun tüm hikayesi — timestamp tuzağı, race condition anatomisi ve neden 3 kişi 2 günde bulamadı:

[Medium'da oku](#)
