package com.example.myorgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myorgs.dao.ProdutosDao
import com.example.myorgs.databinding.ActivityFormularioProdutoBinding
import com.example.myorgs.extensions.tentaCarregarImagem
import com.example.myorgs.model.Produto
import com.example.myorgs.ui.dialog.FormularioImagemDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }
    private var url: String? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseCrashlytics: FirebaseCrashlytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        firebaseCrashlytics = FirebaseCrashlytics.getInstance()
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(true)


        title = "Cadastrar produto"
        configuraBotaoSalvar()
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this)
                .mostra(url) { imagem ->
                    url = imagem
                    binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
                }
        }
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar
        val dao = ProdutosDao()
        botaoSalvar.text = "salvar produto"
        botaoSalvar.setOnClickListener {


            val produtoNovo = criaProduto()

            dao.adiciona(produtoNovo)

         try{
            val params = Bundle()
            params.putString("produto_nome", produtoNovo.nome)
            params.putString("produto_descricao", produtoNovo.descricao)
            params.putString("produto_valor", produtoNovo.valor.toString())
            firebaseAnalytics.logEvent("produto_cadastrado", params)

            finish()
         } catch (e: Exception) {
             firebaseCrashlytics.recordException(e)
             }
            // provoca o crash
            throw RuntimeException("salvar produto")
        }
    }

    private fun criaProduto(): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()
        val campoDescricao = binding.activityFormularioProdutoDescricao
        val descricao = campoDescricao.text.toString()
        val campoValor = binding.activityFormularioProdutoValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        return Produto(
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
    }

}