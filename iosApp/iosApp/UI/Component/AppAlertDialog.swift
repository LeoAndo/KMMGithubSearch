//
//  AppAlertDialog.swift
//  iosApp
//
//  Created by LeoAndo on 2023/03/13.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct AppAlertDialog: View {
    @State var openDialog = false
    var title: String = "Alert"
    var positiveButtonText: String = "OK"
    var negativeButtonText: String = "Cancel"
    var message: String = "dettail message"
    let onNegativeButtonTap: (() -> Void)
    let onPositiveButtonTap: (() -> Void)
    var body: some View {
        VStack {
            
        }
        .alert(title, isPresented: $openDialog){
            Button(negativeButtonText){
                openDialog = false
                onNegativeButtonTap()
            }
            Button(positiveButtonText){
                openDialog = false
                onPositiveButtonTap()
            }
        } message: {
            Text(message)
        }
    }
}

struct AppAlertDialog_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AppAlertDialog(onNegativeButtonTap: {}, onPositiveButtonTap: {})
            AppAlertDialog(openDialog: true,onNegativeButtonTap: {}, onPositiveButtonTap: {})
        }
        
    }
}
