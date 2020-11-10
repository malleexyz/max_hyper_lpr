#import "HyperLprPlugin.h"
#import "CameraViewController.h"

@implementation HyperLprPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"hyper_lpr"
                                     binaryMessenger:[registrar messenger]];
    HyperLprPlugin* instance = [[HyperLprPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"getPlatformVersion" isEqualToString:call.method]) {
        result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
    } else  if ([@"startScan" isEqualToString:call.method]) {
        
        CameraViewController* video = [CameraViewController new];
        video.resultCB = ^(NSString* text, UIImage* image) {
            // self.imageView.image = image;
            // self.resultLabel.text = text;
            result(text);
        };
        video.modalPresentationStyle = UIModalPresentationFullScreen;
        [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:video animated:YES completion:nil];
        
    } else {
        result(FlutterMethodNotImplemented);
    }
}

- (void)openCamera
{
    
}

@end
