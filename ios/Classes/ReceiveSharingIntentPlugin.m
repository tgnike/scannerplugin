#import "ReceiveSharingIntentPlugin.h"
#import <receive_scanner_intent/receive_scanner_intent-Swift.h>

@implementation ReceiveSharingIntentPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftReceiveSharingIntentPlugin registerWithRegistrar:registrar];
}
@end
