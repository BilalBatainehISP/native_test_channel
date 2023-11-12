import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:screenshot/screenshot.dart';

class PrintingWidget extends StatelessWidget {
   PrintingWidget({Key? key}) : super(key: key);

  ScreenshotController screenshotController = ScreenshotController();
  @override
  Widget build(BuildContext context) {
    return  Scaffold(
      appBar: AppBar(title: const Text("Printing Widget"),),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [

              FittedBox(
                fit: BoxFit.contain,
                child: Screenshot(
                  controller: screenshotController,
                 // color: Colors.amber,
                  child: Column(
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text("For Assistance ",style: TextStyle(fontSize: 20),),
                          Text("للحصول على المساعدة",style: TextStyle(fontSize: 20),),
                        ],
                      ),

                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Image.asset("images/flutter.png",scale: 9.5,),
                          Text("Tel : 920004550",style: TextStyle(fontSize: 22,fontWeight: FontWeight.w600),),
                        ],
                      ),

                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text("Terminal ID: ",style: TextStyle(fontSize: 20),),
                          Text("81818181",style: TextStyle(fontSize: 22,fontWeight: FontWeight.w600),),
                          Text(": رقم الجهاز",style: TextStyle(fontSize: 20,),),
                        ],
                      ),
                    ],
                  ),
                ),
              ),


            ],
          ),
        ),
      ),

      floatingActionButton: FloatingActionButton(
        onPressed: (){
          screenshotController.captureFromWidget(Column(
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("For Assistance ",style: TextStyle(fontSize: 20),),
                  Text("للحصول على المساعدة",style: TextStyle(fontSize: 20),),
                ],
              ),

              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Image.asset("images/flutter.png",scale: 9.5,),
                  Text("Tel : 920004550",style: TextStyle(fontSize: 22,fontWeight: FontWeight.w600),),
                ],
              ),

              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("Terminal ID: ",style: TextStyle(fontSize: 20),),
                  Text("81818181",style: TextStyle(fontSize: 22,fontWeight: FontWeight.w600),),
                  Text(": رقم الجهاز",style: TextStyle(fontSize: 20,),),
                ],
              ),
            ],
          ),delay: const Duration(milliseconds: 10))
              .then((Uint8List image)async{
            final directory = await getApplicationDocumentsDirectory();
            final imagePath = await File('${directory.path}/image.png').create();
            await imagePath.writeAsBytes(image);

            print(imagePath.path);
          });
        },
      child: Icon(Icons.print),),

    );
  }
}
