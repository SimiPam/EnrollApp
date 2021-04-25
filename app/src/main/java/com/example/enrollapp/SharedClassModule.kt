package com.example.enrollapp
//import org.koin.core.module.Module
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module

val sharedclassmodule = module {
//    single { PaperPrefs(androidApplication()) }
//    single { DateLiveData() }
 //   single { BasicAWSCredentials(Constants.KEY, Constants.SECRET) }
    single <Uploader>{  UploaderImpl(context = androidApplication(),basicAWSCredentials =get() ) }
}