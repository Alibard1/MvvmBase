# MvvmBase


1 [Prefance](#prefance)

2 [BaseBindFragment](#basebindfragment)

3 [BaseViewModel](#baseviewmodel)

### Prefance 

Base MVVM library. Consist Base Activity and Fragment for data bindig ( with Dagger Inhection) 

### BaseBindFragment

```java
BaseBindFragment<T : ViewDataBinding, V : BaseViewModel<*>>
```

It take in params ViewDataBinding and ViewModel. Atomaticaly apply it to your layout.

To set you layout need set this field.

```java
abstract val layoutId: Int
```

To set you indingVariable need set this field from your BR.

```java
abstract fun getBindingVariable(): Int
```

### BaseViewModel

Project consisnt with BaseViewModel that already have CompositeDisposable what automatucaly creared at the end of lifecycle.

```java
BaseViewModel<N>
```

It take Navigato as param. 
