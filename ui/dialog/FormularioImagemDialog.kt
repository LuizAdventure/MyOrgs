package com.example.myorgs.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.myorgs.databinding.FormularioImagemBinding
import com.example.myorgs.extensions.tentaCarregarImagem

class FormularioImagemDialog(private val context: Context) {

    fun mostra(
        urlPadrao: String? = null,
        quandoImagemCarragada: (imagem: String) -> Unit
    )  {
        FormularioImagemBinding
            .inflate(LayoutInflater.from(context)).apply {

                urlPadrao?.let {
                    formularioImagemImageview.tentaCarregarImagem(it)
                    formularioImagemUrl.setText(it)
                }

                formularioImagemBotaoCarregar.setOnClickListener {
                    val url = formularioImagemUrl.text.toString()
                    formularioImagemImageview.tentaCarregarImagem(url)
                }

                AlertDialog.Builder(context)
                    .setView(root)
                    .setPositiveButton("Confirmar") { _, _ ->
                        val url = formularioImagemUrl.text.toString()
                        quandoImagemCarragada(url)
                    }
                    .setNegativeButton("Cancelar") { _, _ ->

                    }
                    .show()
            }




    }

}